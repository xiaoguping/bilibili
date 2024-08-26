package com.imooc.bilibili.service;

import com.imooc.bilibili.domain.auth.*;
import com.imooc.bilibili.domain.constant.AuthRoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 作者：xgp
 * 时间：2024/4/24
 * 描述：
 */

@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;
    public UserAuthorities getUserAuthorities(Long userId) {
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<Long> userRoleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        List<AuthRoleElementOperation> roleElementOperationList = authRoleService.getRoleElementOperationsByRoleIds(userRoleIds);

        List<AuthRoleMenu> roleMenuList = authRoleService.getRoleMenusByRoleIds(userRoleIds);

        return new UserAuthorities(roleElementOperationList,roleMenuList);
    }

    public void addDefaultRole(Long userId) {
        UserRole userRole = new UserRole();
        AuthRole authRole = authRoleService.getRoleByCode(AuthRoleConstant.ROLE_LV0);
        userRole.setRoleId(authRole.getId());
        userRole.setUserId(userId);
        userRoleService.addUserRole(userRole);

    }
}
