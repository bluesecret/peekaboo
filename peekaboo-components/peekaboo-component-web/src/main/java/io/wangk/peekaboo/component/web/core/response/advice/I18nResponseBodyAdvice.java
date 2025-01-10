package io.wangk.peekaboo.component.web.core.response.advice;

import io.wangk.peekaboo.component.web.core.response.ResponseProperties;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdvicePredicate;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdviceProcessor;
import io.wangk.peekaboo.component.web.core.response.data.Response;
import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 国际化处理
 *
 * @author feiniaojin
 */
@Order(2000)
@ControllerAdvice
@ConditionalOnProperty(prefix = ResponseProperties.PREFIX, name = "i18n", havingValue = "true")
public class I18nResponseBodyAdvice extends AbstractResponseBodyAdvice implements ResponseBodyAdvicePredicate, ResponseBodyAdviceProcessor {

    private static final String[] EMPTY_ARRAY = new String[0];

    @Resource
    private ResponseProperties properties;

    @Resource
    private MessageSource grMessageSource;

    @Override
    public Object process(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Response res) {
            Locale locale = LocaleContextHolder.getLocale();
            ResponseStatus bodyStatus = res.getStatus();
            String code = bodyStatus.getCode();
            String renderMsg = grMessageSource.getMessage(code, EMPTY_ARRAY, null, locale);
            //有国际化配置的才会替换，否则使用默认配置的
            if (StringUtils.hasText(renderMsg)) {
                bodyStatus.setMsg(renderMsg);
            }
        }
        return body;
    }

    @Override
    public boolean shouldApplyTo(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        return properties.getI18n();
    }

    @PostConstruct
    public void init() {
        CopyOnWriteArrayList<ResponseBodyAdvicePredicate> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(this);
        this.setPredicates(copyOnWriteArrayList);
        this.setResponseBodyAdviceProcessor(this);
    }
}
