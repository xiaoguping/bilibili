package com.imooc.bilibili.service;


import com.imooc.bilibili.dao.AuthRoleElementOperationDao;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 作者：xgp
 * 时间：2024/4/24
 * 描述：
 */

@Service
public class AuthRoleElementOperationService {

    @Autowired
    AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> userRoleIds) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(userRoleIds);
    }
}
