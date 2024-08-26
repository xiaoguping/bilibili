package com.imooc.bilibili.api;

import com.imooc.bilibili.api.support.UserSupport;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.service.ElasticSearchService;
import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.service.util.RSAUtil;
import com.sun.org.apache.xerces.internal.dom.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.EscapedErrors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */
@RestController
public class UserApi {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping("/user-allDetail")
    public JsonResponse<User> getUsers() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String pk =  RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    //获取用户信息
    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }


    //新用户注册
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    //用户登录
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
       String token = userService.login(user);
       return new JsonResponse<>(token);
    }

    //登陆接口2，返回双token
    @PostMapping("/user-dts")
    public JsonResponse<Map<String,Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String,Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    //退出登陆接口
    @DeleteMapping("/refresh-tokens")
    public JsonResponse<String> logOut(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logOut(refreshToken,userId);
        return JsonResponse.success();
    }

    //刷新accessToken：无感地保持用户登陆状态
    @PostMapping("/access-tokens")
    public JsonResponse<String> accessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUser(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUser(user);
        return JsonResponse.success();
    }

    @Transactional
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userinfo) {
        Long userId = userSupport.getCurrentUserId();
        userinfo.setUserId(userId);
        userService.updateUserInfos(userinfo);
        elasticSearchService.addUserInfo(userinfo);
        return JsonResponse.success();
    }


}
