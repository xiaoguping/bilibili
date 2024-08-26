package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作者：xgp
 * 时间：2024/4/26
 * 描述：
 */

@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String roleCode);
}
