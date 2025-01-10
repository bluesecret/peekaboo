package io.wangk.peekaboo.common.exception;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

public enum Status {

    SUCCESS("200", "success", "成功"),
    BAD_REQUEST("400", "Bad Request", "错误的请求"),
    UNAUTHORIZED("401", "Unauthorized", "账号未登录"),
    FORBIDDEN("403","Forbidden", "没有该操作权限"),
    NOT_FOUND("404","Not Found", "请求未找到"),
    METHOD_NOT_ALLOWED("405","Method Not Allowed", "请求方法不正确"),
    TOO_MANY_REQUESTS("429" , "Too Many Requests", "请求过于频繁，请稍后重试"),
    INTERNAL_SERVER_ERROR ("500","INTERNAL_SERVER_ERROR", "系统异常"),
    NOT_IMPLEMENTED("501","NOT_IMPLEMENTED", "功能未实现/未开启"),
    ERROR_CONFIGURATION("502","ERROR_CONFIGURATION", "错误的配置项"),


    /**
     * 自定义错误段
     * xx-xx-xxxx
     * 10 系统内置
     * - 01 系统
     * - 02 用户
     * 11 工作空间
     * 12 数据源
     * 13 任务
     * 14 SLA 告警
     */
    REQUEST_ERROR("10010001", "Request Error", "请求错误"),
    INVALID_TOKEN("10010002", "Invalid Token ：{0}", "无效的Token ：{0}"),
    TOKEN_IS_NULL_ERROR("10010003", "Token is Null Error", "Token为空错误"),
    PLEASE_LOGIN("10010004", "Please login", "请登录"),
    TOKEN_NAME_EXIST("10010005", "Token name exist", "Token名称重复"),
    TOKEN_NO_EXIST("10010006", "Token no exist", "Token不存在错误"),
    TOKEN_EXPIRED("10010007", "Token no exist", "Token已过期"),

    USERNAME_HAS_BEEN_REGISTERED_ERROR("10020001", "The username {0} has been registered", "用户名 {0} 已被注册过"),
    REGISTER_USER_ERROR("10020002", "Register User {0} Error", "注册用户{0}失败"),
    USERNAME_OR_PASSWORD_ERROR("10020003", "Username or Email Error", "用户名或者密码错误"),
    USER_IS_NOT_EXIST("10020004", "User is not exist", "用户不存在"),
    CREATE_VERIFICATION_IMAGE_ERROR("10020005", "create verification image error", "创建验证码错误"),
    EXPIRED_VERIFICATION_CODE("10020006", "expired verification code", "验证码已过期，请重新刷新"),
    INVALID_VERIFICATION_CODE("10020007", "invalid verification code", "错误的验证码，请重新输入"),
    OLD_PASSWORD_IS_INCORRECT_ERROR("10020008", "Old Password is Incorrect", "旧密码错误"),
    NEW_PASSWORD_CONFIRM_IS_INCORRECT_ERROR("10020009", "New Password Confirm is Incorrect", "新密码确认错误"),
    USER_IS_DISABLED("10020010", "User is disabled", "用户已被禁用"),

    DICT_TYPE_NOT_EXISTS("10030001", "Dict type not exists", "字典类型不存在"),
    DICT_TYPE_NAME_DUPLICATE("10030002", "Dict type name duplicate", "字典类型名称已存在"),
    DICT_TYPE_TYPE_DUPLICATE("10030003", "Dict type duplicate", "字典类型已存在"),
    DICT_TYPE_DISABLE("10030004", "Dict type disable", "字典类型已禁用"),
    DICT_DATA_VALUE_DUPLICATE("10030005", "Dict data value duplicate", "字典值已存在"),
    DICT_DATA_NOT_EXISTS("10030006", "Dict data not exists", "字典值不存在"),

    CREATE_CONFIG_ERROR("22010001", "Create Config {0} Error", "创建参数 {0} 错误"),
    CONFIG_NOT_EXIST_ERROR("22010002", "Config {0} Not Exist Error", "参数 {0} 不存在错误"),
    CONFIG_EXIST_ERROR("22010003", "Config {0} is Exist error", "参数 {0} 已存在错误"),
    UPDATE_CONFIG_ERROR("22010004", "Update Config {0} Error", "更新参数 {0} 错误"),

    CAN_NOT_DELETE_DEFAULT_CONFIG_ERROR("22010005", "Can Not Delete Default Config {0} Error", "不能删除默认参数 {0} 错误"),
    ;
    private final String code;
    private final String enMsg;
    private final String zhMsg;

    Status(String code, String enMsg, String zhMsg) {
        this.code = code;
        this.enMsg = enMsg;
        this.zhMsg = zhMsg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage())) {
            return this.zhMsg;
        } else {
            return this.enMsg;
        }
    }

    /**
     * Retrieve Status enum entity by status code.
     */
    public static Optional<Status> findStatusBy(String code) {
        for (Status status : Status.values()) {
            if (code == status.getCode()) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
