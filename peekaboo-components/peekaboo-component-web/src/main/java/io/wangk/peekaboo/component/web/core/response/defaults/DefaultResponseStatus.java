package io.wangk.peekaboo.component.web.core.response.defaults;


import io.wangk.peekaboo.component.web.core.response.data.ResponseStatus;

/**
 * 默认的ResponseStatus实现
 *
 */
public class DefaultResponseStatus implements ResponseStatus {

    /**
     * 响应码.
     */
    private String code;

    /**
     * 响应信息.
     */
    private String msg;

    public DefaultResponseStatus() {
    }

    /**
     * 通过响应码和响应信息构造枚举.
     *
     * @param code 响应码
     * @param msg  响应信息
     */
    public DefaultResponseStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
