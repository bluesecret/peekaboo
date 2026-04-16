package io.wangk.peekaboo.component.web.core.response.api;

import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;

public interface ResponseStatusFactory {
    /**
     * 获得响应成功的ResponseMeta.
     *
     * @return
     */
    ResponseStatus defaultSuccess();

    /**
     * 获得失败的ResponseMeta.
     *
     * @return
     */
    ResponseStatus defaultError();


    /**
     * 从code和msg创建ResponseStatus
     * @param code
     * @param msg
     * @return
     */
    ResponseStatus newInstance(String code,String msg);

}
