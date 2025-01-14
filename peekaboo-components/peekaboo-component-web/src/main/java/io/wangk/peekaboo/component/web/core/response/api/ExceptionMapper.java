package io.wangk.peekaboo.component.web.core.response.api;


import java.lang.annotation.*;


/**
 * 异常映射注解.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExceptionMapper {

    /**
     * 异常对应的错误码.
     *
     * @return 异常对应的错误码
     */
    String code() default "ERROR";

    /**
     * 异常信息.
     *
     * @return 异常对应的提示信息
     */
    String msg() default "Poor network quality!";

    /**
     * 异常信息是否支持替换
     * 仅当msgReplaceable==ture，且异常实例的message不为空时才能替换
     *
     * @return
     */
    boolean msgReplaceable() default false;
}
