package io.wangk.peekaboo.component.record.aspect;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import io.wangk.peekaboo.common.utils.ServletUtils;
import io.wangk.peekaboo.common.utils.SpringUtils;
import io.wangk.peekaboo.common.utils.json.JsonUtils;
import io.wangk.peekaboo.component.record.annotation.Record;
import io.wangk.peekaboo.component.record.enums.BusinessStatus;
import io.wangk.peekaboo.component.record.event.RecordEvent;
import io.wangk.peekaboo.component.satoken.core.util.LoginUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 操作日志记录处理
 *
 * @author Lion Li
 */
@Slf4j
@Aspect
public class RecordAspect {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };


    /**
     * 计时 key
     */
    private static final ThreadLocal<StopWatch> KEY_CACHE = new ThreadLocal<>();

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(record)")
    public void doBefore(JoinPoint joinPoint, Record record) {
        StopWatch stopWatch = new StopWatch();
        KEY_CACHE.set(stopWatch);
        stopWatch.start();
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(record)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Record record, Object jsonResult) {
        handleLog(joinPoint, record, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(record)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Record record, Exception e) {
        handleLog(joinPoint, record, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Record record, final Exception e, Object jsonResult) {
        try {

            // *========数据库日志=========*//
            RecordEvent event = new RecordEvent();
            event.setTenantId(LoginUserHolder.getTenantId());
            event.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = ServletUtils.getClientIP();
            event.setIp(ip);
            event.setUrl(StringUtils.substring(Objects.requireNonNull(ServletUtils.getRequest()).getRequestURI(), 0, 255));
            event.setOperator(LoginUserHolder.getUsername());
            event.setDept(LoginUserHolder.getDeptName());

            if (e != null) {
                event.setStatus(BusinessStatus.FAIL.ordinal());
                event.setError(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            event.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            event.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, record, event, jsonResult);
            // 设置消耗时间
            StopWatch stopWatch = KEY_CACHE.get();
            stopWatch.stop();
            event.setCostTime(stopWatch.getTime());
            // 发布事件保存数据库
            SpringUtils.publishEvent(event);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            KEY_CACHE.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param record     日志
     * @param event 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Record record, RecordEvent event, Object jsonResult) throws Exception {
        // 设置action动作
        event.setBusinessType(record.businessType().ordinal());
        // 设置标题
        event.setTitle(record.title());
        // 是否需要保存request，参数和值
        if (record.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, event, record.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (record.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
            event.setResult(StringUtils.substring(JsonUtils.toJsonString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param event 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, RecordEvent event, String[] excludeParamNames) throws Exception {
        Map<String, String> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = event.getRequestMethod();
        if (MapUtil.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            event.setRequestParams(StringUtils.substring(params, 0, 2000));
        } else {
            MapUtil.removeAny(paramsMap, EXCLUDE_PROPERTIES);
            MapUtil.removeAny(paramsMap, excludeParamNames);
            event.setRequestParams(StringUtils.substring(JsonUtils.toJsonString(paramsMap), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringJoiner params = new StringJoiner(" ");
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString();
        }
        for (Object o : paramsArray) {
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                String str = JsonUtils.toJsonString(o);
                Dict dict = JsonUtils.parseMap(str);
                if (MapUtil.isNotEmpty(dict)) {
                    MapUtil.removeAny(dict, EXCLUDE_PROPERTIES);
                    MapUtil.removeAny(dict, excludeParamNames);
                    str = JsonUtils.toJsonString(dict);
                }
                params.add(str);
            }
        }
        return params.toString();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                return value instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
               || o instanceof BindingResult;
    }
}
