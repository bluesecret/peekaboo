package io.wangk.peekaboo.component.satoken.core.context;

import cn.dev33.satoken.spring.SaTokenContextForSpringInJakartaServlet;
import cn.dev33.satoken.spring.pathmatch.SaPatternsRequestConditionHolder;

/**
 * 自定义 SaTokenContext 实现类，重写 matchPath 方法，切换为 ant_path_matcher 模式，使之可以支持 `**` 之后再出现内容
 */
public class SaTokenContextByPatternsRequestCondition extends SaTokenContextForSpringInJakartaServlet {

    @Override
    public boolean matchPath(String pattern, String path) {
        return SaPatternsRequestConditionHolder.match(pattern, path);
    }

}