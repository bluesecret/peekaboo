package io.wangk.peekaboo.component.web.core.response.data;

public interface Response {

    /**
     * 设置响应行
     *
     * @param statusLine 状态行
     */
    void setStatus(ResponseStatus statusLine);

    /**
     * 获取响应行
     *
     * @return
     */
    ResponseStatus getStatus();

    /**
     * 设置响应数据.
     *
     * @param payload 设置的响应数据.
     */
    void setPayload(Object payload);

    /**
     * 获得响应数据.
     *
     * @return
     */
    Object getPayload();
}
