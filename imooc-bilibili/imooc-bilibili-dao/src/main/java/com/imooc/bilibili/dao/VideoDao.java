package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/5/3
 * 描述：
 */

@Mapper
public interface VideoDao {

    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

    Integer pageCountVideos(Map<String, Object> params);

    List<Video> pageListVideos(Map<String, Object> params);

    void addVideoLike(VideoLike videoLike);

    VideoLike getVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    Video getVideoById(Long videoId);

    void deleteVideoLike(@Param("userId")Long userId, @Param("videoId")Long videoId);

    Long getVideoLikes(Long videoId);

    void deleteVideoCollection(@Param("userId") Long userId,@Param("videoId") Long videoId);

    void addVideoCollection(VideoCollection videoCollection);

    Long getVideoCollection(Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    VideoCoin getVideoCoinByVideoIdAndUserId(@Param("userId") Long userId, @Param("videoId") Long videoId);

    void addVideoCoin(VideoCoin videoCoin);

    void updateVideoCoin(VideoCoin videoCoin);

    Long getVideoCoinsAmount(Long videoId);

    void addVideoComments(VideoComment videoComment);

    Integer pageCountVideoComments(Map<String, Object> params);

    List<VideoComment> pageListVideoComments(Map<String, Object> params);

    List<VideoComment> batchGetCommentsByRootIds(List<Long> rootIdList);

    Video getVideoDetails(Long videoId);

    VideoView getVideoView(Map<String, Object> params);

    void addVideoView(VideoView videoView);

    Integer getVideoViewCounts(Long videoId);

    List<UserPreference> getAllUserPreference();

    List<Video> batchGetVideosByIds(@Param("idList") List<Long> idList);
}
