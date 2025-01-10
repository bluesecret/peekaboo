package io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * 异常处理器
 *
 * @author wangk
 */
public interface ResponseBodyAdviceProcessor {

    /**
     * ResponseBodyAdviceProcessor 处理
     *
     * @param body 请求体
     * @param returnType 返回类型
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    Object process(Object body,
                   MethodParameter returnType,
                   MediaType selectedContentType,
                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                   ServerHttpRequest request,
                   ServerHttpResponse response);
}
