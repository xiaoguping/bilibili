package com.imooc.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 作者：xgp
 * 时间：2024/5/5
 * 描述：
 */

@Mapper
public interface UserCoinDao {

    Integer getUserCoinsAmount(Long userId);

    void updateUserCoinAmount(@Param("userId") Long userId, @Param("balanceCoin") int balanceCoin, @Param("updateTime") Date updateTime);
}
