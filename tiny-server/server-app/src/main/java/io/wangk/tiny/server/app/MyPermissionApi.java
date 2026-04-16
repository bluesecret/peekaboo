package io.wangk.tiny.server.app;

import io.wangk.tiny.common.api.PermissionApi;
import io.wangk.tiny.common.pojo.DataPermissionInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyPermissionApi implements PermissionApi {
    @Override
    public DataPermissionInfo getDataPermissionInfo(Long userId) {
        return null;
    }

    @Override
    public List<String> findPermissionList(String roleCode) {
        return List.of();
    }

    @Override
    public List<String> findRoleList(Object userId) {
        return List.of();
    }
}
