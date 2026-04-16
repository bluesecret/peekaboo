package io.wangk.peekaboo.component.web.core.response.advice;

import io.wangk.peekaboo.common.exception.StatusException;
import io.wangk.peekaboo.component.web.core.response.ResponseProperties;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception.ControllerAdviceHttpProcessor;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception.ControllerAdvicePredicate;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.exception.ControllerAdviceProcessor;
import io.wangk.peekaboo.component.web.core.response.api.ResponseFactory;
import io.wangk.peekaboo.component.web.core.response.api.ResponseStatusFactory;
import io.wangk.peekaboo.component.web.core.response.data.Response;
import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Response框架异常处理，处理GracefulException
 *
 * @author wangk
 */
@Order(200)
@ControllerAdvice
public class FrameworkExceptionAdvice extends AbstractControllerAdvice
        implements ControllerAdvicePredicate, ControllerAdviceProcessor,
        ControllerAdviceHttpProcessor {

    @Resource
    private ResponseFactory responseFactory;

    @Resource
    private ResponseProperties properties;

    @Resource
    private ResponseStatusFactory responseStatusFactory;

    @Override
    public Response process(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception exception) {
        if(exception instanceof StatusException statusException){
            ResponseStatus statusLine = fromStatusExceptionInstance(statusException);
            return responseFactory.newInstance(statusLine);
        }
        return null;
    }

    private ResponseStatus fromStatusExceptionInstance(StatusException exception) {
        String code = exception.getCode();
        if (code == null) {
            code = properties.getDefaultErrorCode();
        }
        return responseStatusFactory.newInstance(code, exception.getMsg());
    }

    @Override
    @ExceptionHandler(value = StatusException.class)
    public Object exceptionHandler(Exception exception) {
        return super.exceptionHandler(exception);
    }

    @Override
    public boolean shouldApplyTo(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception exception) {
        return exception instanceof StatusException;
    }
}


