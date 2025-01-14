package io.wangk.peekaboo.component.datapermission.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import io.wangk.peekaboo.component.datapermission.core.aop.DataPermissionAnnotationAdvisor;
import io.wangk.peekaboo.component.datapermission.core.db.DataPermissionRuleHandler;
import io.wangk.peekaboo.component.datapermission.core.rule.DataPermissionRule;
import io.wangk.peekaboo.component.datapermission.core.rule.DataPermissionRuleFactory;
import io.wangk.peekaboo.component.datapermission.core.rule.DataPermissionRuleFactoryImpl;
import io.wangk.peekaboo.component.mybatis.core.util.MyBatisUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 数据权限的自动配置类
 *
 * @author 芋道源码
 */
@AutoConfiguration
public class YudaoDataPermissionAutoConfiguration {

    @Bean
    public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
        return new DataPermissionRuleFactoryImpl(rules);
    }

    @Bean
    public DataPermissionRuleHandler dataPermissionRuleHandler(MybatisPlusInterceptor interceptor,
                                                               DataPermissionRuleFactory ruleFactory) {
        // 创建 DataPermissionInterceptor 拦截器
        DataPermissionRuleHandler handler = new DataPermissionRuleHandler(ruleFactory);
        DataPermissionInterceptor inner = new DataPermissionInterceptor(handler);
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return handler;
    }

    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }

}
