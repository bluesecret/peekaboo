package io.wangk.peekaboo.component.web.core.desensitize.slider.handler;

import io.wangk.peekaboo.component.web.core.desensitize.slider.annotation.CarLicenseDesensitize;

/**
 * {@link CarLicenseDesensitize} 的脱敏处理器
 *
 * @author gaibu
 */
public class CarLicenseDesensitization extends AbstractSliderDesensitizationHandler<CarLicenseDesensitize> {

    @Override
    Integer getPrefixKeep(CarLicenseDesensitize annotation) {
        return annotation.prefixKeep();
    }

    @Override
    Integer getSuffixKeep(CarLicenseDesensitize annotation) {
        return annotation.suffixKeep();
    }

    @Override
    String getReplacer(CarLicenseDesensitize annotation) {
        return annotation.replacer();
    }

    @Override
    public String getDisable(CarLicenseDesensitize annotation) {
        return annotation.disable();
    }

}
