package io.wangk.peekaboo.component.web.core.response.defaults;


import io.wangk.peekaboo.component.web.core.response.ResponseProperties;
import io.wangk.peekaboo.component.web.core.response.api.ResponseStatusFactory;
import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;
import jakarta.annotation.Resource;

/**
 * 提供的默认的ResponseMetaFactory实现.
 *
 */
public class DefaultResponseStatusFactoryImpl implements ResponseStatusFactory {

    @Resource
    private ResponseProperties properties;

    @Override
    public ResponseStatus defaultSuccess() {

        DefaultResponseStatus defaultResponseStatus = new DefaultResponseStatus();
        defaultResponseStatus.setCode(properties.getDefaultSuccessCode());
        defaultResponseStatus.setMsg(properties.getDefaultSuccessMsg());
        return defaultResponseStatus;
    }

    @Override
    public ResponseStatus defaultError() {
        DefaultResponseStatus defaultResponseStatus = new DefaultResponseStatus();
        defaultResponseStatus.setCode(properties.getDefaultErrorCode());
        defaultResponseStatus.setMsg(properties.getDefaultErrorMsg());
        return defaultResponseStatus;
    }

    @Override
    public ResponseStatus newInstance(String code, String msg) {
        return new DefaultResponseStatus(code, msg);
    }
}
