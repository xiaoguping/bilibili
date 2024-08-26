package com.imooc.bilibili.api;

import com.imooc.bilibili.api.support.UserSupport;
import com.imooc.bilibili.domain.*;
import com.imooc.bilibili.service.DanmuService;
import com.imooc.bilibili.service.ElasticSearchService;
import com.imooc.bilibili.service.VideoService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/5/3
 * 描述：
 */
@RestController
public class VideoApi {

    @Autowired
    private VideoService videoService;

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private ElasticSearchService elasticSearchService;


    @Autowired
    private UserSupport userSupport;

    //上传视频
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideos(video);

        //向es中添加视频
        elasticSearchService.addVideo(video);

        return JsonResponse.success();
    }

    //分页查询视频
    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size, Integer no,String area){
        PageResult<Video> result= videoService.pageListVideo(size,no,area);
        return new JsonResponse<>(result);
    }


    //在线浏览视频
    //todo
    @GetMapping("/video-slices")
    //通过流的形式返回视频内容，流会写入响应输出流中
    //因此这个接口不需要返回值
    //url为文件分片在文件服务器的相对地址（例如url:L21MN2YzjaKEISgKAAAAAF2AerU754.mp4）
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String url) throws Exception {
        videoService.viewVideoOnlineBySlices(request,response,url);
    }


    //视频点赞
    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(userId,videoId);
        return JsonResponse.success();
    }

    //取消视频点赞
    @DeleteMapping("/video-likes")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(userId,videoId);
        return JsonResponse.success();
    }

    //查询视频点赞数量
    @GetMapping("/video-likes")
    public JsonResponse<Map<String,Object>> listVideoLikes(@RequestParam Long videoId){
        //游客模式下，也可以观看视频
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception e){}
        Map<String,Object> result = videoService.getVideoLikes(videoId,userId);
        return new JsonResponse<>(result);
    }

    //收藏视频
    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCollection(userId,videoCollection);
        return JsonResponse.success();
    }
    //取消收藏
    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(userId,videoId);
        return JsonResponse.success();
    }
    //查询视频收藏总数量
    @GetMapping("/video-collections")
    public JsonResponse<Map<String,Object>> getVideoCollections(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception e){}
        Map<String,Object> result = videoService.getVideoCollections(videoId,userId);
        return new JsonResponse<>(result);
    }

    //视频投币
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCoins(userId,videoCoin);
        return JsonResponse.success();
    }

    //查询视频总投币数量
    @GetMapping("/video-coins")
    public JsonResponse<Map<String,Object>> getVideoCoins(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }
        Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
        return new JsonResponse<>(result);
    }

    //增加视频评论
    @PostMapping("/video-comments")
    public JsonResponse<String> addVideoComments(@RequestBody VideoComment videoComment){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoComments(userId,videoComment);
        return JsonResponse.success();
    }

    //分页查询视频评论
    @GetMapping("/video-comments")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
                                                                        @RequestParam Integer no,
                                                                        @RequestParam Long videoId){
        PageResult<VideoComment> result = videoService.pageListVideoComments(size,no,videoId);
        return new JsonResponse<>(result);
    }

    //视频详情
    @GetMapping("/video-details")
    public JsonResponse<Map<String,Object>> getVideoDetails(@RequestParam Long videoId){
        Map<String,Object> result = videoService.getVideoDetails(videoId);
        return new JsonResponse<>(result);
    }


    //添加视频观看记录
    @PostMapping("/video-views")
    public JsonResponse<String> addVideoViews(@RequestBody VideoView videoView,
                                              HttpServletRequest request){
        Long userId;
        try{
            userId = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
            videoService.addVideoView(videoView,request);
        }catch (Exception e){
            //游客模式添加观看记录
            videoService.addVideoView(videoView,request);
        }

        return JsonResponse.success();
    }

    //查询视频播放量
    @GetMapping("/video-view-counts")
    public JsonResponse<Integer> getVideoViewCounts(@RequestParam Long videoId){
        Integer count = videoService.getVideoViewCounts(videoId);
        return new JsonResponse<>(count);
    }

    //视频内容推荐
    //todo
    @GetMapping("/recommendations")
    public JsonResponse<List<Video>> recommend()throws TasteException {
        Long userId = userSupport.getCurrentUserId();
        List<Video> list = videoService.recommend(userId);
        return new JsonResponse<>(list);
    }


    //根据关键词查询视频
    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
        Video video = elasticSearchService.getVideos(keyword);

        return new JsonResponse<>(video);
    }
}
