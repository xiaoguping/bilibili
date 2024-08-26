package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.VideoDao;
import com.imooc.bilibili.domain.*;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.service.util.FastDFSUtil;
import com.imooc.bilibili.service.util.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericItemPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：xgp
 * 时间：2024/5/3
 * 描述：
 */

@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;
    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private UserService userService;

    @Transactional
    public void addVideos(Video video) {
        Date now = new Date();
        video.setCreateTime(now);
        videoDao.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> tagList = video.getVideoTagList();
        tagList.forEach(item -> {
            item.setVideoId(videoId);
            item.setCreateTime(now);
        });
        videoDao.batchAddVideoTags(tagList);
    }

    public PageResult<Video> pageListVideo(Integer size, Integer no, String area) {
        if(size == null || no == null) {
            throw new ConditionException("参数异常！");
        }

        Map<String,Object> params = new HashMap<>();
        params.put("start",(no - 1)*size);
        params.put("limit",size);
        params.put("area",area);

        List<Video> videoList = new ArrayList<>();

        Integer total = videoDao.pageCountVideos(params);
        if(total > 0) {
            videoList = videoDao.pageListVideos(params);
        }

        return new PageResult<>(total, videoList);

    }

    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        fastDFSUtil.viewVideoOnlineBySlices(request,response,path);
    }

    public void addVideoLike(Long userId, Long videoId) {
        Video video =videoDao.getVideoById(videoId);
        if(video == null) {
            throw new ConditionException("非法视频！");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId,userId);
        if(videoLike != null) {
            throw new ConditionException("已经赞过！");
        }

        videoLike = new VideoLike();
        videoLike.setCreateTime(new Date());
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoDao.addVideoLike(videoLike);
    }

    public void deleteVideoLike(Long userId, Long videoId) {
        videoDao.deleteVideoLike(userId,videoId);
    }

    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Long count = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId,userId);
        boolean like = videoLike != null;
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("like", like);
        return map;
    }
    @Transactional
    public void addVideoCollection(Long userId, VideoCollection videoCollection) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if(videoId == null || groupId == null) {
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null) {
            throw new ConditionException("非法视频！");
        }
        videoDao.deleteVideoCollection(videoId,userId);
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoDao.addVideoCollection(videoCollection);

    }

    public void deleteVideoCollection(Long userId, Long videoId) {
        videoDao.deleteVideoCollection(userId,videoId);
    }

    public Map<String, Object> getVideoCollections(Long videoId, Long userId) {

        Long count = videoDao.getVideoCollection(videoId);
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserId(videoId,userId);
        boolean like = videoCollection != null;
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);

        //提示前端若是登陆用户要对收藏按钮进行特殊处理，表示当前用户喜欢
        map.put("like", like);
        return map;
    }

    @Transactional
    public void addVideoCoins(Long userId, VideoCoin videoCoin) {
        Long videoId = videoCoin.getVideoId();
        Integer amount = videoCoin.getAmount();
        if(videoId == null || amount == null) {
            throw new ConditionException("参数错误！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null) {
            throw new ConditionException("非法视频！");
        }

        //查询用户当前拥有的硬币数量
        Integer userCoinsAmount = userCoinService.getUserCoinsAmount(userId);
        userCoinsAmount = userCoinsAmount == null ? 0 : userCoinsAmount;
        if(amount > userCoinsAmount){
            throw new ConditionException("硬币数量不足！");
        }

        //查询当前用户对该视频已经投了多少硬币
        VideoCoin dbVideoCoin = videoDao.getVideoCoinByVideoIdAndUserId(userId,videoCoin.getVideoId());


        //第一次投币：新增记录
        if(dbVideoCoin == null){
            videoCoin.setCreateTime(new Date());
            videoCoin.setUserId(userId);
            videoDao.addVideoCoin(videoCoin);
        }else{
            //非第一次投币：更新记录
            videoCoin.setUserId(userId);
            videoCoin.setAmount(dbVideoCoin.getAmount() + amount);
            videoCoin.setUpdateTime(new Date());
            videoDao.updateVideoCoin(videoCoin);
        }

        //更新当前用户的硬币总数
        userCoinService.updateUserCoinAmount(userId,userCoinsAmount - amount);

    }

    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        Long count = videoDao.getVideoCoinsAmount(videoId);
        VideoCoin videoCoin = videoDao.getVideoCoinByVideoIdAndUserId(userId,videoId);
        boolean like = videoCoin != null;
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("like", like);
        return map;
    }

    public void addVideoComments(Long userId, VideoComment videoComment) {
        Long videoId = videoComment.getVideoId();
        if(videoId == null) {
            throw new ConditionException("参数错误！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null) {
            throw new ConditionException("非法视频");
        }
        videoComment.setUserId(userId);
        videoComment.setCreateTime(new Date());

        videoDao.addVideoComments(videoComment);

    }

    public PageResult<VideoComment> pageListVideoComments(Integer size, Integer no, Long videoId) {
        Video video = videoDao.getVideoById(videoId);
        if(video == null) {
            throw new ConditionException("非法视频！");
        }

        Map<String,Object> params = new HashMap<>();
        params.put("start",(no-1)*size);
        params.put("limit",size);
        params.put("videoId",videoId);

        Integer total = videoDao.pageCountVideoComments(params);
        List<VideoComment> list = new ArrayList<>();

        if(total > 0){
            //查询该页的所有一级评论
            list = videoDao.pageListVideoComments(params);
            //批量查询二级评论
            List<Long> parentIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            List<VideoComment> childCommentList = videoDao.batchGetCommentsByRootIds(parentIdList);
            //批量查询用户信息
            Set<Long> userIdSet = list.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            Set<Long> replyUserIdSet = childCommentList.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            userIdSet.addAll(replyUserIdSet);
            List<UserInfo> userInfoList = userService.batchGetUserInfoByUserIds(userIdSet);
            Map<Long,UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId,userInfo -> userInfo));
            list.forEach(comment ->{
                //获取每个一级评论的id
                Long id = comment.getId();

                //每一个一级评论的二级评论
                List<VideoComment> childList = new ArrayList<>();
                childCommentList.forEach(child ->{
                    if(id.equals(child.getRootId())){
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });

                comment.setChildCommentList(childList);
                comment.setUserInfo(userInfoMap.get(comment.getUserId()));
            });
        }

        return new PageResult<>(total,list);

    }

    public Map<String, Object> getVideoDetails(Long videoId) {
        Video video = videoDao.getVideoDetails(videoId);
        if(video == null) {
            throw new ConditionException("参数错误！");
        }
        Long userId = video.getUserId();
        User user = userService.getUserInfo(userId);
        UserInfo userInfo = user.getUserInfo();
        Map<String, Object> result = new HashMap<>();
        result.put("video", video);
        result.put("user", userInfo);
        return result;


    }

    public void addVideoView(VideoView videoView, HttpServletRequest request) {
        Long userId = videoView.getUserId();
        Long videoId = videoView.getVideoId();

        //生成clientId
        String agent = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        String clientId = String.valueOf(userAgent.getId());
        String ip = IpUtil.getIP(request);
        Map<String,Object> params = new HashMap<>();

        //记录用户观看记录：记录userId
        //记录游客观看记录：记录客户端Id和ip
        if(userId != null){
            params.put("userId",userId);
        }else{
            params.put("ip",ip);
            params.put("clientId",clientId);
        }

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        params.put("today",sdf.format(now));
        params.put("videoId",videoId);

        //添加观看记录
        VideoView dbvideoView = videoDao.getVideoView(params);
        if(dbvideoView == null){
            videoView.setClientId(clientId);
            videoView.setIp(ip);
            videoView.setCreateTime(new Date());
            videoDao.addVideoView(videoView);
        }

    }

    public Integer getVideoViewCounts(Long videoId) {
        return videoDao.getVideoViewCounts(videoId);
    }

    public List<Video> recommend(Long userId) throws TasteException {
        List<UserPreference> list = videoDao.getAllUserPreference();
        DataModel dataModel = this.createDataModel(list);
        //获取用户相似程度
        UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
        System.out.println(similarity.userSimilarity(11, 12));
        //获取用户邻居
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
        long[] ar = userNeighborhood.getUserNeighborhood(userId);
        //构建推荐器
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
        //推荐视频
        List<RecommendedItem> recommendedItems = recommender.recommend(userId, 5);
        List<Long> itemIds = recommendedItems.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
        return videoDao.batchGetVideosByIds(itemIds);
    }

    private DataModel createDataModel(List<UserPreference> userPreferenceList) {
        FastByIDMap<PreferenceArray> fastByIDMap = new FastByIDMap<>();
        Map<Long, List<UserPreference>> map = userPreferenceList.stream().collect(Collectors.groupingBy(UserPreference::getUserId));
        Collection<List<UserPreference>> list = map.values();
        for(List<UserPreference> userPreferences : list){
            GenericPreference[] array = new GenericPreference[userPreferences.size()];
            for(int i = 0;i < userPreferences.size();i++){
                UserPreference userPreference = userPreferences.get(i);
                GenericPreference item = new GenericPreference(userPreference.getUserId(),userPreference.getVideoId(),userPreference.getValue());
                array[i] = item;
            }
            fastByIDMap.put(array[0].getUserID(),new GenericItemPreferenceArray(Arrays.asList(array)));
        }
        return new GenericDataModel(fastByIDMap);

    }
}
