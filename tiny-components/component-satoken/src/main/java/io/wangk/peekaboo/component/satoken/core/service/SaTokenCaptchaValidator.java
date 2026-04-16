package io.wangk.peekaboo.component.satoken.core.service;

import cn.dev33.satoken.stp.StpLogic;
import io.wangk.peekaboo.common.exception.Assert;
import io.wangk.peekaboo.common.utils.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaTokenCaptchaValidator {

    private StpLogic stpLogic;

    public String createToken(String code, long timeout) {
        return stpLogic.createTokenValue(code, "captchaImage", timeout, null);
    }

    public void validCode(String token, String code) {
        String originalCode = StringUtils.toString(stpLogic.getLoginIdByToken(token));
        Assert.isFalse(StringUtils.isEmpty(originalCode), "验证码已失效，请刷新重试！");
        Assert.isTrue(StringUtils.equals(originalCode.toLowerCase(), code.toLowerCase()), "验证码不一致，请刷新重试！");
    }
}
