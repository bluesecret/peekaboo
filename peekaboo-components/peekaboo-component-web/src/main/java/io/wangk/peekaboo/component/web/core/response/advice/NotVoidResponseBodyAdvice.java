package io.wangk.peekaboo.component.web.core.response.advice;

import io.wangk.peekaboo.component.web.core.response.ResponseProperties;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdvicePredicate;
import io.wangk.peekaboo.component.web.core.response.advice.lifecycle.response.ResponseBodyAdviceProcessor;
import io.wangk.peekaboo.component.web.core.response.api.ResponseFactory;
import io.wangk.peekaboo.component.web.core.response.data.Response;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 非空返回值的处理.
 *
 */
@Order(value = 1000)
@ControllerAdvice
public class NotVoidResponseBodyAdvice extends AbstractResponseBodyAdvice implements ResponseBodyAdvicePredicate,
        ResponseBodyAdviceProcessor {

    private final Logger logger = LoggerFactory.getLogger(NotVoidResponseBodyAdvice.class);

    @Resource
    private ResponseFactory responseFactory;

    @Resource
    private ResponseProperties properties;

    @Resource
    private AdviceSupport adviceSupport;

    /**
     * 路径过滤器
     */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public Object process(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return responseFactory.newSuccessInstance();
        } else if (body instanceof Response) {
            return body;
        } else {
            if (logger.isDebugEnabled()) {
                String path = request.getURI().getPath();
                logger.debug("Response:非空返回值，执行封装:path={}", path);
            }
            return responseFactory.newSuccessInstance(body);
        }
    }

    @Override
    public boolean shouldApplyTo(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {

        Method method = methodParameter.getMethod();

        //method为空、返回值为void、非JSON，直接跳过
        if (Objects.isNull(method)
                || method.getReturnType().equals(Void.TYPE)
                || method.getReturnType().equals(Response.class)
                || !adviceSupport.isJsonHttpMessageConverter(clazz)) {
            logger.debug("Response:method为空、返回值为void和Response类型、非JSON，跳过");
            return false;
        }

        //命中@ExcludeFromResponse注解，返回false，不处理
        if (adviceSupport.matchExcludeResponse(method)) {
            return false;
        }

        //配置了例外包路径，则该路径下的controller都不再处理
        List<String> excludePackages = properties.getExcludePackages();
        if (!CollectionUtils.isEmpty(excludePackages)) {
            // 获取请求所在类的的包名
            String packageName = method.getDeclaringClass().getPackage().getName();
            if (excludePackages.stream().anyMatch(item -> ANT_PATH_MATCHER.match(item, packageName))) {
                logger.debug("Response:匹配到excludePackages例外配置，跳过:packageName={},", packageName);
                return false;
            }
        }

        //配置了例外的返回类型，则不处理
        Set<Class<?>> excludeReturnTypes = properties.getExcludeReturnTypes();
        if (!CollectionUtils.isEmpty(excludeReturnTypes)
                && excludeReturnTypes.contains(method.getReturnType())) {
            logger.debug("Response:匹配到excludeReturnTypes例外配置，跳过:returnType={},", method.getReturnType());
            return false;
        }

        List<String> excludeUrls = properties.getExcludeUrls();
        if (!CollectionUtils.isEmpty(excludeUrls)) {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestUri = request.getRequestURI();
            for (String excludeUrl : excludeUrls) {
                if (ANT_PATH_MATCHER.match(excludeUrl, requestUri)) {
                    logger.debug("Response:匹配到excludeUrls例外配置，跳过:excludeUrl={},requestURI={}",
                            excludeUrl, requestUri);
                    return false;
                }
            }
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return false;
        }

        //配置了异常放行，就不会再封装了
        Exception releaseException = (Exception) requestAttributes.getAttribute(ReleaseExceptionHandlerExceptionResolver.RELEASE_EXCEPTION_KEY,
                RequestAttributes.SCOPE_REQUEST);
        if (Objects.nonNull(releaseException)
                && adviceSupport.isMatchExcludeException(releaseException)) {
            return false;
        }

        logger.debug("Response:非空返回值，需要进行封装");
        return true;
    }

    @PostConstruct
    public void init() {
        CopyOnWriteArrayList<ResponseBodyAdvicePredicate> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(this);
        this.setPredicates(copyOnWriteArrayList);
        this.setResponseBodyAdviceProcessor(this);
    }
}
