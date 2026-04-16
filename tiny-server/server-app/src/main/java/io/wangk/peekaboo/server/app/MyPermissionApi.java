package io.wangk.peekaboo.server.app;

import io.wangk.peekaboo.common.api.PermissionApi;
import io.wangk.peekaboo.common.pojo.DataPermissionInfo;
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
