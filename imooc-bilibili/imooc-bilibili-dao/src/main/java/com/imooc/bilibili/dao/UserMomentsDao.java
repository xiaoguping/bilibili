package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作者：xgp
 * 时间：2024/4/23
 * 描述：
 */

@Mapper
public interface UserMomentsDao {
    public void addUserMoments(UserMoment userMoment);
}
