package io.wangk.peekaboo.component.web.core.response;

import io.wangk.peekaboo.common.exception.StatusException;

/**
 * 拒绝处理的异常
 *
 * @author wangk
 */
public class ResponseRejectException extends StatusException {

    public ResponseRejectException() {
    }

    public ResponseRejectException(String msg) {
        super(msg);
    }

    public ResponseRejectException(String code, String msg) {
        super(code, msg);
    }

    public ResponseRejectException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResponseRejectException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

}
