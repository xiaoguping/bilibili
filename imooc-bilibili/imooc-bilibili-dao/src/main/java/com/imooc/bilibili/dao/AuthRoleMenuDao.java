package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 作者：xgp
 * 时间：2024/4/24
 * 描述：
 */

@Mapper
public interface AuthRoleMenuDao {
    List<AuthRoleMenu> getRoleMenusByRoleIds(@Param("userRoleIds") Set<Long> userRoleIds);
}
