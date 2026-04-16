package io.wangk.peekaboo.component.satoken.config;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import io.wangk.peekaboo.common.api.PermissionApi;
import io.wangk.peekaboo.component.satoken.core.context.SaTokenContextByPatternsRequestCondition;
import io.wangk.peekaboo.component.satoken.core.service.SaTokenCaptchaValidator;
import io.wangk.peekaboo.component.satoken.core.service.StpInterfaceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
@EnableConfigurationProperties(SaTokenProperties.class)
public class SaTokenConfiguration {


    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (无状态模式)
        return new StpLogicJwtForStateless();
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    public StpInterface stpInterface(PermissionApi permissionApi) {
        return new StpInterfaceImpl(permissionApi);
    }

    @Bean
    @Primary
    public SaTokenContext saTokenContext() {
        return new SaTokenContextByPatternsRequestCondition();
    }

    @Bean
    public SaTokenCaptchaValidator captchaValidator(StpLogic stpLogic) {
        return new SaTokenCaptchaValidator(stpLogic);
    }

}
