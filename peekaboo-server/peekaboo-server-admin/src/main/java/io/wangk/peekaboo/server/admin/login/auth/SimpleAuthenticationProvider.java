package io.wangk.peekaboo.server.admin.login.auth;

import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemUserDto;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemRoleService;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemUserService;
import cn.ruixi.azure.rainbow.boot.security.core.service.AuthenticationProvider;
import cn.ruixi.azure.rainbow.boot.security.core.service.bo.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author bijie
 * @since 2024/3/22
 */
@Component
public class SimpleAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SystemUserService systemUserApi;
    @Autowired
    private SystemRoleService systemRoleApi;
    @Override
    public Authentication loadUserByUsername(String username) {
        Authentication authentication = new Authentication();
        SystemUserDto userDetailsByUserName = systemUserApi.getSystemUserDetailsByUserName(username);
        authentication.setUsername(userDetailsByUserName.getUsername());
        authentication.setPassword(userDetailsByUserName.getPassword());
        authentication.setEnabled(true);
        authentication.setAccountNonLocked(true);
        authentication.setAccountNonExpired(true);
        Set<String> userRoleList = systemRoleApi.getUserRoleList(userDetailsByUserName.getId());
        authentication.setAuthorities(userRoleList);
        return authentication;
    }
}
