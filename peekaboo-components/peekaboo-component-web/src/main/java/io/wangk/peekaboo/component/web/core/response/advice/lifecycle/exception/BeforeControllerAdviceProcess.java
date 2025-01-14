package io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

/**
 * 接受异常的处理器
 *
 * @author wangk
 */
public interface BeforeControllerAdviceProcess {

    /**
     * ControllerAdvice处理前的回调
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    void call(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
}
