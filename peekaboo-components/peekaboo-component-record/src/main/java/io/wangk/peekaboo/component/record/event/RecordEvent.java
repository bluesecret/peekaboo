package io.wangk.peekaboo.component.record.event;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志事件
 *
 * @author Lion Li
 */

@Data
public class RecordEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    private Long id;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer businessType;

    /**
     * 业务类型数组
     */
    private Integer[] businessTypes;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    private Integer type;

    /**
     * 操作人员
     */
    private String operator;

    /**
     * 部门名称
     */
    private String dept;

    /**
     * 请求url
     */
    private String url;

    /**
     * 操作地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 返回参数
     */
    private String result;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String error;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 消耗时间
     */
    private Long costTime;
}
