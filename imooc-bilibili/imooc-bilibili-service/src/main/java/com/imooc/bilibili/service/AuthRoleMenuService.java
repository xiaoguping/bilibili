package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.AuthRoleMenuDao;
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
public class AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;
    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> userRoleIds) {
        return authRoleMenuDao.getRoleMenusByRoleIds(userRoleIds);
    }
}
