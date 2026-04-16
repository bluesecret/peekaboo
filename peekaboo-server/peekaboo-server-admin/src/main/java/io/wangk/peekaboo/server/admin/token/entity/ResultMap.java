package io.wangk.peekaboo.server.admin.token.entity;

import cn.ruixi.azure.rainbow.boot.module.system.enums.Status;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.constant.DataFlexConstants;
import cn.ruixi.azure.rainbow.boot.security.core.util.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;


/**
 * @author bijie
 * @since 2024/5/23
 */
public class ResultMap extends HashMap<String, Object> {

    public static final String EMPTY = "";

    private int code;

    private JwtTokenManager tokenManager;

    public ResultMap() {
    }

    public ResultMap(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public ResultMap success() {
        this.code = 200;
        this.put("code", this.code);
        this.put("msg", "Success");
        this.put("data", EMPTY);
        return this;
    }


    public ResultMap successAndRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(DataFlexConstants.TOKEN_HEADER_STRING);
        if(StringUtils.isEmpty(token)) {
            token = (String)request.getAttribute(DataFlexConstants.TOKEN_HEADER_STRING);
        }
        this.code = Status.SUCCESS.getCode();
        this.put("code", this.code);
        this.put("msg", "Success");
        this.put("token", this.tokenManager.refreshToken(token));
        this.put("data", EMPTY);
        return this;
    }


    public ResultMap message(String message) {
        this.put("msg", message);
        return this;
    }

    public ResultMap payload(Object object) {
        this.put("data", null == object ? EMPTY : object);
        return this;
    }

    public int getCode() {
        return code;
    }
}