package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/4/21
 * 描述：
 */
@Mapper
public interface UserFollowingDao {
    Integer deleteUserFollowing(@Param("userId") Long userId, @Param("followingId") Long followingId);

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);

    List<FollowingGroup> getUserFollowingGroups(Long userId);
}
