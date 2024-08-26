package com.imooc.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.dao.UserDao;
import com.imooc.bilibili.domain.*;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.domain.constant.UserConstant;
import com.imooc.bilibili.service.util.MD5Util;
import com.imooc.bilibili.service.util.RSAUtil;
import com.imooc.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */

@Service

public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Transactional
    public void addUser(User user) {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = userDao.getUserByPhone(phone);
        if(dbUser != null){
            throw new ConditionException("该手机号已经注册！");
        }

        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);

        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_FEMALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
        //添加用户默认权限角色
        userAuthService.addDefaultRole(user.getId());

        //将新用户信息加入es
        elasticSearchService.addUserInfo(userInfo);
    }

    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = userDao.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("当前用户不存在！");
        }

        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8"  );
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }

        //生成用户令牌
        return TokenUtil.generateToken(dbUser.getId());
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo  userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUser(User user) throws Exception {
        String phone = user.getPhone();
        if(!StringUtils.isNullOrEmpty(phone)) {
            User dbUser = userDao.getUserByPhone(phone);
            if(dbUser != null && dbUser.getId()!= user.getId()){
                throw new ConditionException("该手机号已注册！");
            }
        }

        if(!StringUtils.isNullOrEmpty(user.getPassword())) {
            //更新密码时，先将密码还原为原始密码
            String rawPassword = RSAUtil.decrypt(user.getPassword());


            String salt = user.getSalt();
            String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

            user.setPassword(md5Password);
        }


        user.setUpdateTime(new Date());
        userDao.updateUser(user);
    }

    public void updateUserInfos(UserInfo userinfo) {
        userinfo.setUpdateTime(new Date());
        userDao.updateUserInfos(userinfo);
    }


    public User getUserById(Long followingId) {
        return userDao.getUserById(followingId);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserInfoByUserIds(userIdList);
    }

    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        Integer total = userDao.pageCountUserInfos(params);

        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            list = userDao.pageListUserInfos(params);
        }

        return new PageResult<>(total, list);

    }

    @Transactional
    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = userDao.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("当前用户不存在！");
        }

        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8"  );
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }

        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);

        //将refreshToken保存到数据库
        userDao.deleteRefreshToken(refreshToken,userId);
        userDao.addRefreshToken(refreshToken,userId,new Date());

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    public void logOut(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken,userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail == null){
            throw new ConditionException("555","token过期！");
        }

        //验证refreshToken的有效期
        Long userId = TokenUtil.verifyToken(refreshTokenDetail.getRefreshToken());

        //删除旧的refreshToken
        userDao.deleteRefreshToken(refreshToken,userId);

        String newRefreshToken = TokenUtil.generateToken(userId);

        //新增新的newRefreshToken
        userDao.addRefreshToken(newRefreshToken,userId,new Date());
//        Long userId = refreshTokenDetail.getUserId();
        return newRefreshToken;
    }

    public List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdSet) {
        return userDao.batchGetUserInfoByUserIds(userIdSet);
    }

    //查询所有UserInfo
    public List<UserInfo> getAllUserInfo() {
        return userDao.getAllUserInfo();
    }

    public List<Video> getAllVideo() {
        return userDao.getAllVideo();
    }
}
