package io.wangk.peekaboo.component.web.core.response;

import cn.hutool.db.Page;
import io.wangk.peekaboo.common.exception.Status;
import io.wangk.peekaboo.component.web.core.response.api.ResponseFactory;
import io.wangk.peekaboo.component.web.core.response.data.Response;
import io.wangk.peekaboo.component.web.core.response.defaults.DefaultResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Response工具类
 *
 * @author wangk
 */
public class Responses {

    private static ResponseFactory responseFactory;

    Responses(ResponseFactory responseFactory) {
        Responses.responseFactory = responseFactory;
    }

    public static Response unauthorized() {
        return ofStatus(Status.UNAUTHORIZED);
    }

    public static Response forbidden() {
        return ofStatus(Status.FORBIDDEN);
    }

    public static Response ofSuccess(Object data, Page page) {
        Map<String, Object> a = new HashMap<>();
        a.put("list", data);
        a.put("page", page);
        return ofSuccess(a);
    }

    public static Response ofSuccess() {
        return ofSuccess(null);
    }

    public static Response ofSuccess(Object data) {
        return responseFactory.newSuccessInstance(data);
    }

    public static Response ofError(String msg) {
        Response response = responseFactory.newFailInstance();
        response.getStatus().setMsg(msg);
        return response;
    }

    public static Response ofStatus(Status status) {
        return ofStatus(status.getCode(), status.getMsg());
    }

    public static Response ofStatus(String code, String msg) {
        return newResponse(code, msg, "");
    }

    private static Response newResponse(String code, String msg, Object data) {
        return responseFactory.newInstance(new DefaultResponseStatus(code, msg), data);
    }

}
