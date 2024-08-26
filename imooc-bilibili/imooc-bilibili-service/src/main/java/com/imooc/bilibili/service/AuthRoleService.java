package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.AuthRoleDao;
import com.imooc.bilibili.domain.auth.AuthRole;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
import com.imooc.bilibili.domain.auth.AuthRoleMenu;
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
public class AuthRoleService {

    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    private AuthRoleDao authRoleDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> userRoleIds) {
        return authRoleElementOperationService.getRoleElementOperationsByRoleIds(userRoleIds);
    }

    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> userRoleIds) {
        return authRoleMenuService.getRoleMenusByRoleIds(userRoleIds);
    }

    public AuthRole getRoleByCode(String roleCode) {
        return authRoleDao.getRoleByCode(roleCode);
    }
}
