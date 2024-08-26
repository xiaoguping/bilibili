package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.UserRoleDao;
import com.imooc.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/4/24
 * 描述：
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;
    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleDao.getUserRoleByUserId(userId);
    }

    public void addUserRole(UserRole userRole) {
        userRole.setCreateTime(new Date());
        userRoleDao.addUserRole(userRole);
    }
}
