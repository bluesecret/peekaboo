package io.wangk.peekaboo.component.satoken.core.service;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpInterface;
import io.wangk.peekaboo.common.api.PermissionApi;

import java.util.ArrayList;
import java.util.List;

public class StpInterfaceImpl implements StpInterface {

    private PermissionApi permissionApi;

    public StpInterfaceImpl(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 1. 声明权限码集合
        List<String> list = new ArrayList<>();

        // 2. 遍历角色列表，查询拥有的权限码
        for (String roleCode : getRoleList(loginId, loginType)) {
            List<String> permissionList = (List<String>) SaManager.getSaTokenDao().getObject("sa-token:role-find-permission:" + roleCode);
            if (permissionList == null) {
                // 从数据库查询这个角色 id 所拥有的权限列表
                // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
                permissionList = this.permissionApi.findPermissionList(roleCode);
                SaManager.getSaTokenDao().setObject("sa-token:role-find-permission:" + roleCode, permissionList, 60 * 60 * 24 * 30);
            }
            list.addAll(permissionList);
        }
        // 3. 返回权限码集合
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = (List<String>) SaManager.getSaTokenDao().getObject("sa-token:loginId-find-role:" + loginId);
        if (roleList == null) {
            roleList = permissionApi.findRoleList(loginId);
            SaManager.getSaTokenDao().setObject("sa-token:loginId-find-role:" + loginId, roleList, 60 * 60 * 24 * 30);
        }
        return roleList;
    }
}
