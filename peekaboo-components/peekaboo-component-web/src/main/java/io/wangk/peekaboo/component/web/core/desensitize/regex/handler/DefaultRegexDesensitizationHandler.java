package io.wangk.peekaboo.component.web.core.desensitize.regex.handler;

import io.wangk.peekaboo.component.web.core.desensitize.regex.annotation.RegexDesensitize;

/**
 * {@link RegexDesensitize} 的正则脱敏处理器
 *
 * @author gaibu
 */
public class DefaultRegexDesensitizationHandler extends AbstractRegexDesensitizationHandler<RegexDesensitize> {

    @Override
    String getRegex(RegexDesensitize annotation) {
        return annotation.regex();
    }

    @Override
    String getReplacer(RegexDesensitize annotation) {
        return annotation.replacer();
    }

    @Override
    public String getDisable(RegexDesensitize annotation) {
        return annotation.disable();
    }

}
