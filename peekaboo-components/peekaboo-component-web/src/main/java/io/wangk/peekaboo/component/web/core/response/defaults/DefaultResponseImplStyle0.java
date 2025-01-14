package io.wangk.peekaboo.component.web.core.response.defaults;


import io.wangk.peekaboo.component.web.core.response.data.Response;
import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;

import java.util.Collections;

/**
 * 默认的Response实现
 * 包装成统一响应的JavaBean.
 *
 */
public class DefaultResponseImplStyle0 implements Response {

    private ResponseStatus status;
    
    private Object payload = Collections.emptyMap();

    public DefaultResponseImplStyle0() {
    }

    public DefaultResponseImplStyle0(Object payload) {
        this.payload = payload;
    }

    @Override
    public void setStatus(ResponseStatus responseStatus) {
        this.status = responseStatus;
    }

    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    @Override
    public void setPayload(Object obj) {
        this.payload = obj;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
