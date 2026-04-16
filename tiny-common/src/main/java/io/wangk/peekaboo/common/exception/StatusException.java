package io.wangk.peekaboo.common.exception;

public class StatusException extends PeekabooException {

    private String code;

    private String msg;

    public StatusException() {
    }

    public StatusException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public StatusException(Status status) {
        this(status.getCode(), status.getMsg());
    }

    public StatusException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public StatusException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public StatusException(Status status, Throwable cause) {
        this(status.getCode(), status.getMsg(), cause);
    }

    public StatusException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
