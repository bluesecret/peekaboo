package io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception;

import io.wangk.peekaboo.component.web.core.response.data.Response;
import org.springframework.http.ResponseEntity;

/**
 * 计算异常对应的HTTP处理器
 *
 * @author wangk
 */
public interface ControllerAdviceHttpProcessor {

    /**
     * 生成ResponseEntity
     *
     * @param response  GR定义的响应体
     * @param throwable 对应的异常
     * @return ResponseEntity，包括header/状态码/响应数据
     */
    default ResponseEntity<Response> process(Response response, Throwable throwable) {
        return ResponseEntity.ofNullable(response);
    }
}
