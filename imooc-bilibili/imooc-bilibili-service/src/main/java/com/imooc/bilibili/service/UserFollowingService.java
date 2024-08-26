package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.UserFollowingDao;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserFollowing;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.constant.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 作者：xgp
 * 时间：2024/4/21
 * 描述：
 */

@Service
public class UserFollowingService {

    @Autowired
    UserFollowingDao userFollowingDao;

    @Autowired
    FollowingGroupService followingGroupService;

    @Autowired
    UserService userService;

    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();

        if(groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        }else{
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if(followingGroup == null) {
                throw new ConditionException("关注的分组不存在！");
            }
        }

        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if(user == null) {
            throw new ConditionException("关注的用户不存在！");
        }


        //关注逻辑：先删除再关注，这样在关注用户的时候不用判断用户是否已经被关注
        //如果没有匹配到删除的记录，什么也不执行，也不会报错
        //需要保证：删除关注和新增关注是一个原子操作,加事务
        userFollowingDao.deleteUserFollowing(userFollowing.getId(),followingId);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);

    }

    //第一步：获取关注的用户列表
    //第二步：根据用户的id查询关注用户的基本信息
    //第三步：将关注用户按关注分组进行分类（哪些用户是特别关注，哪些是悄悄关注，哪些是默认关注，哪些是自定义关注）List<UserInfo> userInfoList = userService.getUserInfoByUserIds(followingIdSet);
//    public List<FollowingGroup> getUserFollowings(Long userId) {
//
//        //拿到所有关注记录
//        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
//
//        //获取所有关注用户的用户id
//        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
//
//        List<UserInfo> userInfoList = new ArrayList<>();
//        if(followingIdSet.size() > 0) {
//            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
//        }
//
//
//        //向每一条关注记录，加入对应关注用户的基本信息。
//        for(UserFollowing userFollowing : list) {
//            for(UserInfo userInfo : userInfoList) {
//                if(userFollowing.getFollowingId().equals(userInfo.getUserId())) {
//                    userFollowing.setUserInfo(userInfo);
//                }
//            }
//        }
//
//
//        //获取用户的所有关注分组（系统初始分组 + 自定义分组）
//        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
//
//        //增加一个全部分组，将所有关注用户放入集合，存入分组中
//        FollowingGroup allGroup = new FollowingGroup();
//        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
//        allGroup.setFollowingUserInfoList(userInfoList);
//
//        List<FollowingGroup> result = new ArrayList<>();
//        result.add(allGroup);
//
//        for(FollowingGroup group : groupList) {
//            List<UserInfo> infoList = new ArrayList<>();
//            for(UserFollowing userFollowing : list) {
//                if(userFollowing.getGroupId().equals(group.getId())) {
//                    infoList.add(userFollowing.getUserInfo());
//                }
//            }
//
//            group.setFollowingUserInfoList(infoList);
//            result.add(group);
//        }
//
//        return result;
//    }

    //获取用户列表，按关注分组显示
    public List<FollowingGroup> getUserFollowings(Long userId){
        //获取所有用户关注的用户记录
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        //从所有用户记录中提取关注的用户id
        Set<Long> followingIds = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        //根据followingIds查询到对应的UserInfo
        List<UserInfo> userInfoList = new ArrayList<>();
        if(followingIds.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(followingIds);
        }

        for(UserFollowing userFollowing:list){
            for(UserInfo userInfo:userInfoList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }

        List<FollowingGroup> result = new ArrayList<>();
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setFollowingUserInfoList(userInfoList);
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        result.add(allGroup);

        List<FollowingGroup> groups = followingGroupService.getByUserId(userId);
        for(FollowingGroup group : groups) {

            List<UserInfo> groupUserInfoList = new ArrayList<>();
            for(UserFollowing userFollowing: list){
                if(userFollowing.getGroupId().equals(group.getId())){
                    groupUserInfoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(groupUserInfoList);
            result.add(group);
        }


        return result;

    }

    //获取用户粉丝列表
    /*1.根据 userId == followingId 查询用户的粉丝列表
        select * from t_user_following where followingId = #{userId}

      2.根据用户粉丝列表中的userId 查询用户信息
        select * from t_user_info where #{userId}

      3.对每一位粉丝判断，当前用户是否也关注了粉丝：是，互粉。否：粉丝
     */

    public List<UserFollowing> getUserFans(Long userId){
        List<UserFollowing> fanslist = userFollowingDao.getUserFans(userId);
        Set<Long> fansIds = fanslist.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());

        List<UserInfo> userInfoList = new ArrayList<>();
        if(fansIds.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(fansIds);
        }

        for(UserFollowing fans:fanslist){
            for(UserInfo userInfo:userInfoList){
                if(fans.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false);
                    fans.setUserInfo(userInfo);
                }
            }
        }

        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);
        for(UserFollowing fans:fanslist){
            for(UserFollowing following:followingList){
                if(fans.getUserId().equals(following.getFollowingId())){
                    fans.getUserInfo().setFollowed(true);
                }
            }
        }

        return fanslist;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return userFollowingDao.getUserFollowingGroups(userId);
    }


    //若搜索出来的用户，被当前用户关注过，则将userinfo的followed字段设置为true
    public List<UserInfo> checkFollowingStatus(List<UserInfo> list, Long userId) {
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for(UserFollowing userFollowing:userFollowingList){
            for(UserInfo userInfo:list){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(true);
                }else{
                    userInfo.setFollowed(false);
                }
            }
        }

        return list;
    }

}
