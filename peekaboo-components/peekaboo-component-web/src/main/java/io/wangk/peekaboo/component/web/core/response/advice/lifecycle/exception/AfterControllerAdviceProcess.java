package io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception;

import io.wangk.peekaboo.component.web.core.response.data.Response;

/**
 * 异常处理后的回调
 *
 * @author qinyujie
 */
public interface AfterControllerAdviceProcess {
    /**
     * 执行处理逻辑之后的回调
     *
     * @param response
     * @param throwable
     */
    void call(Response response, Throwable throwable);
}
