package io.wangk.peekaboo.server.admin.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author bijie
 * @since 2024/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "system_log")
public class SystemLog {
    /**
     * 主键
     */
    private String id;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 日志内容
     */
    private String logContent;
    /**
     * 操作人
     */
    private String username;
    /**
     * 操作人真实名称
     */
    private String nickName;
    /**
     * ip
     */
    private String ipAddr;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 请求类型
     */
    private String requestType;
    /**
     * 耗时
     */
    private BigDecimal costTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 结果
     */
    private String result;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String stopTime;

}
