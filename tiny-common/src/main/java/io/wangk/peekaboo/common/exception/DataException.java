package io.wangk.peekaboo.common.exception;

/**
 * 带数据的异常响应
 *
 * @author twolf
 */
public class DataException extends StatusException {

    /**
     * 响应数据
     */
    private Object data;

    public DataException() {
    }

    public DataException(Object data) {
        this.data = data;
    }

    public DataException(String msg, Object data) {
        super(msg);
        this.data = data;
    }

    public DataException(Status status, Object data) {
        super(status);
        this.data = data;
    }

    public DataException(String code, String msg, Object data) {
        super(code, msg);
        this.data = data;
    }

    public DataException(String msg, Throwable cause, Object data) {
        super(msg, cause);
        this.data = data;
    }

    public DataException(Status status, Throwable cause, Object data) {
        super(status, cause);
        this.data = data;
    }

    public DataException(String code, String msg, Throwable cause, Object data) {
        super(code, msg, cause);
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
