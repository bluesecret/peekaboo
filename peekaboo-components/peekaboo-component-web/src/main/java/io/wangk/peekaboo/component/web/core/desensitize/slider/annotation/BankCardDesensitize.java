package io.wangk.peekaboo.component.web.core.desensitize.slider.annotation;

import io.wangk.peekaboo.component.web.core.desensitize.base.annotation.DesensitizeBy;
import io.wangk.peekaboo.component.web.core.desensitize.slider.handler.BankCardDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 银行卡号
 *
 * @author gaibu
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = BankCardDesensitization.class)
public @interface BankCardDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 6;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 2;

    /**
     * 替换规则，银行卡号; 比如：9988002866797031 脱敏之后为 998800********31
     */
    String replacer() default "*";

    /**
     * 是否禁用脱敏
     *
     * 支持 Spring EL 表达式，如果返回 true 则跳过脱敏
     */
    String disable() default "";

}