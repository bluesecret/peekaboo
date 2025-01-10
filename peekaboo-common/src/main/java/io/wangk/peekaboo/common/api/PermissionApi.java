package io.wangk.peekaboo.common.api;

import io.wangk.peekaboo.common.pojo.DataPermissionInfo;

import java.util.List;

public interface PermissionApi {

    DataPermissionInfo getDataPermissionInfo(Long userId);

    List<String> findPermissionList(String roleCode);

    List<String> findRoleList(Object userId);
}
