package io.wangk.peekaboo.component.web.core.desensitize.slider.handler;

import io.wangk.peekaboo.component.web.core.desensitize.slider.annotation.PasswordDesensitize;

/**
 * {@link PasswordDesensitize} 的码脱敏处理器
 *
 * @author gaibu
 */
public class PasswordDesensitization extends AbstractSliderDesensitizationHandler<PasswordDesensitize> {
    @Override
    Integer getPrefixKeep(PasswordDesensitize annotation) {
        return annotation.prefixKeep();
    }

    @Override
    Integer getSuffixKeep(PasswordDesensitize annotation) {
        return annotation.suffixKeep();
    }

    @Override
    String getReplacer(PasswordDesensitize annotation) {
        return annotation.replacer();
    }

}
