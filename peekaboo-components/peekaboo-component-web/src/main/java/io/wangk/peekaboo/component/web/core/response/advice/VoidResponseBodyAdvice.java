package io.wangk.peekaboo.component.web.core.response.advice;

import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdvicePredicate;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdviceProcessor;
import io.wangk.peekaboo.component.web.core.response.api.ResponseFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 空返回值的拦截处理.
 *
 */
@Order(value = 1000)
@ControllerAdvice
public class VoidResponseBodyAdvice extends AbstractResponseBodyAdvice implements ResponseBodyAdvicePredicate,
        ResponseBodyAdviceProcessor {

    @Resource
    private ResponseFactory responseFactory;

    @Resource
    private AdviceSupport adviceSupport;

    @Override
    public boolean shouldApplyTo(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return Objects.requireNonNull(methodParameter.getMethod()).getReturnType().equals(Void.TYPE)
                && adviceSupport.isJsonHttpMessageConverter(clazz);
    }

    @Override
    public Object process(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return responseFactory.newSuccessInstance();
    }

    @PostConstruct
    public void init(){
        CopyOnWriteArrayList<ResponseBodyAdvicePredicate> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(this);
        this.setPredicates(copyOnWriteArrayList);
        this.setResponseBodyAdviceProcessor(this);
    }

}
