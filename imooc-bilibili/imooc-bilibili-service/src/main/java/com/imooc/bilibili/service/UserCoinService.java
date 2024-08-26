package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.UserCoinDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 作者：xgp
 * 时间：2024/5/5
 * 描述：
 */

@Service
public class UserCoinService {

    @Autowired
    private UserCoinDao userCoinDao;
    public Integer getUserCoinsAmount(Long userId) {
        return userCoinDao.getUserCoinsAmount(userId);
    }

    public void updateUserCoinAmount(Long userId, int balanceCoin) {
        userCoinDao.updateUserCoinAmount(userId,balanceCoin,new Date());
    }
}
