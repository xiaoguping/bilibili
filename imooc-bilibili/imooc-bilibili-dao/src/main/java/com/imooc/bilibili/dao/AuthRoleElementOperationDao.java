package com.imooc.bilibili.dao;
import com.imooc.bilibili.domain.auth.AuthElementOperation;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
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
public interface AuthRoleElementOperationDao {
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(@Param("userRoleIds") Set<Long> userRoleIds);
}
