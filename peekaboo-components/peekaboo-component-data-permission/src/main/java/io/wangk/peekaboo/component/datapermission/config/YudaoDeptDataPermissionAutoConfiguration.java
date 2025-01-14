package io.wangk.peekaboo.component.datapermission.config;

import io.wangk.peekaboo.common.api.PermissionApi;
import io.wangk.peekaboo.common.utils.StringUtils;
import io.wangk.peekaboo.component.datapermission.core.rule.dept.DeptDataPermissionRule;
import io.wangk.peekaboo.component.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import io.wangk.peekaboo.component.satoken.core.context.LoginUser;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 基于部门的数据权限 AutoConfiguration
 *
 * @author 芋道源码
 */
@AutoConfiguration
@ConditionalOnClass(LoginUser.class)
@ConditionalOnBean(value = {PermissionApi.class, DeptDataPermissionRuleCustomizer.class})
public class YudaoDeptDataPermissionAutoConfiguration {

    @Bean
    public DataPermissionProperties dataPermissionProperties() {
        return new DataPermissionProperties();
    }

    @Bean
    public DeptDataPermissionRule deptDataPermissionRule(PermissionApi permissionApi,
                                                         List<DeptDataPermissionRuleCustomizer> customizers) {
        // 创建 DeptDataPermissionRule 对象
        DeptDataPermissionRule rule = new DeptDataPermissionRule(permissionApi);
        // 补全表配置
        customizers.forEach(customizer -> customizer.customize(rule));
        return rule;
    }

    @Bean
    public DeptDataPermissionRuleCustomizer defaultDeptDataPermissionRuleCustomizer(DataPermissionProperties properties) {
        return rule -> {
            properties.getIncludeTables().forEach((k, v) -> {
                if (v == null) {
                    rule.addDeptColumn(k);
                    rule.addUserColumn(k);
                }
                if (StringUtils.isNotEmpty(v.getDeptId())) {
                    rule.addDeptColumn(k, v.getDeptId());
                }
                if (StringUtils.isNotEmpty(v.getUserId())) {
                    rule.addUserColumn(k, v.getUserId());
                }
            });
        };
    }

}
