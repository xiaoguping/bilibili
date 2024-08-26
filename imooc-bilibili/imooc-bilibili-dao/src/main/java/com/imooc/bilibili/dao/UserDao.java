package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.RefreshTokenDetail;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */

@Mapper
public interface UserDao {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    void updateUser(User user);

    void updateUserInfos(UserInfo userinfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer pageCountUserInfos(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(Map<String, Object> params);

    void deleteRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId);

    void addRefreshToken(@Param("refreshToken")String refreshToken, @Param("userId")Long userId, @Param("createTime") Date createTime);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdSet);

    List<UserInfo> getAllUserInfo();

    List<Video> getAllVideo();
}
