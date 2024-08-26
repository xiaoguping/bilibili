package com.imooc.bilibili.api;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.api.support.UserSupport;
import com.imooc.bilibili.domain.*;
import com.imooc.bilibili.service.UserFollowingService;
import com.imooc.bilibili.service.UserService;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/4/22
 * 描述：
 */

@RestController
public class UserFollowingApi {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserService userService;


    //新增用户关注
    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing) {
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }

    //取消用户关注
//    @DeleteMapping("/user-followings")
//    public JsonResponse<String> deleteUserFollowings(@RequestBody UserFollowing userFollowing) {
//        Long userId = userSupport.getCurrentUserId();
//        userFollowing.setUserId(userId);
//        userFollowingService.deleteUserFollowings(userFollowing);
//        return JsonResponse.success();
//    }

    //获取用户关注列表
    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }

    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans() {
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> result = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);
    }

    //新建用户关注分组
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup) {
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }

    //获取用户关注分组
    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> followingGroupList = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(followingGroupList);
    }

    //分页查询用户列表
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no,@RequestParam Integer size,String nick) {
        Long userId = userSupport.getCurrentUserId();

        //JSONObject可以看作是一个Map类
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);

        if(result.getTotal() > 0){
            //若搜索出来的用户，被当前用户关注过，则将userinfo的followed字段设置为true
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(),userId);

            //引用类型内部内容改变，是否需要重新set引用类型呢？不set能起到改变内容的效果吗？
            //经过验证，可以不用set。引用类型作为参数，传入方法中，内容改变，原来的字段自动改变，不用重新set
//            result.setList(checkedUserInfoList);
        }

        return new JsonResponse<>(result);

    }
}
