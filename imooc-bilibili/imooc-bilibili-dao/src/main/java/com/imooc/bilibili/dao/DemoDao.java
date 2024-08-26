package com.imooc.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * 作者：xgp
 * 时间：2024/4/18
 * 描述：
 */

@Mapper
public interface DemoDao {

    public Long query(Long id);
}
