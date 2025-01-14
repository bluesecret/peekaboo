package io.wangk.peekaboo.component.web.core.response.api;

import io.wangk.peekaboo.component.web.core.response.data.Response;
import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;

/**
 * ResponseBean的工厂类，用于生成ResponseBean.
 *
 */
public interface ResponseFactory {


    /**
     * 创建新的空响应.
     *
     * @return
     */
    Response newEmptyInstance();

    /**
     * 创建新的空响应.
     *
     * @param statusLine 响应行信息.
     * @return
     */
    Response newInstance(ResponseStatus statusLine);

    /**
     * 创建带有数据的响应.
     *
     * @param statusLine 响应行信息
     * @param data 响应数据
     * @return
     */
    Response newInstance(ResponseStatus statusLine,Object data);

    /**
     * 创建新的响应.
     *
     * @return
     */
    Response newSuccessInstance();

    /**
     * 从数据中创建成功响应.
     *
     * @param data 响应数据.
     * @return
     */
    Response newSuccessInstance(Object data);

    /**
     * 创建新的失败响应.
     *
     * @return
     */
    Response newFailInstance();

}
