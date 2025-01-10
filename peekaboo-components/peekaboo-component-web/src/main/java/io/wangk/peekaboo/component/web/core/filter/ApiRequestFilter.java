package io.wangk.peekaboo.component.web.core.filter;

import cn.hutool.core.util.StrUtil;
import io.wangk.peekaboo.component.web.config.WebProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤 /admin-api、/app-api 等 API 请求的过滤器
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public abstract class ApiRequestFilter extends OncePerRequestFilter {

    protected final WebProperties webProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只过滤 API 请求的地址
        List<String> apis = webProperties.getApi().stream().map(api -> api.getPrefix()).collect(Collectors.toList());
        return !StrUtil.startWithAny(request.getRequestURI(), apis.toArray(new String[0]));
    }

}
