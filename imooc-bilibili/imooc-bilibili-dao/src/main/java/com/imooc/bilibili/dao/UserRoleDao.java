package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/4/24
 * 描述：
 */

@Mapper
public interface UserRoleDao {
    List<UserRole> getUserRoleByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
