package io.wangk.tiny.common.api;

import io.wangk.tiny.common.pojo.DataPermissionInfo;

import java.util.List;

public interface PermissionApi {

    DataPermissionInfo getDataPermissionInfo(Long userId);

    List<String> findPermissionList(String roleCode);

    List<String> findRoleList(Object userId);
}
