package io.wangk.peekaboo.component.web.core.desensitize.slider.annotation;

import io.wangk.peekaboo.component.web.core.desensitize.base.annotation.DesensitizeBy;
import io.wangk.peekaboo.component.web.core.desensitize.slider.handler.CarLicenseDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 车牌号
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = CarLicenseDesensitization.class)
public @interface CarLicenseDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 3;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 1;

    /**
     * 替换规则，车牌号;比如：粤A66666 脱敏之后为粤A6***6
     */
    String replacer() default "*";

    /**
     * 是否禁用脱敏
     *
     * 支持 Spring EL 表达式，如果返回 true 则跳过脱敏
     */
    String disable() default "";

}
