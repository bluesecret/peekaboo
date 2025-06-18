package io.wangk.peekaboo.component.record.annotation;

import io.wangk.peekaboo.component.record.enums.BusinessType;
import io.wangk.peekaboo.component.record.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author ruoyi
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Record {

    /**
     *  描述
     * @return
     */
    String value() default "";

    /**
     * 描述
     */
    String title() default "";

    /**
     * 是否打印日志
     * @return
     */
    boolean logging() default false;

    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;


    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

}
