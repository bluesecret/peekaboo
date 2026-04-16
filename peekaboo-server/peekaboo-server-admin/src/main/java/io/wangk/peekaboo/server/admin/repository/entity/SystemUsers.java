package io.wangk.peekaboo.server.admin.repository.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户信息表(SystemUsers)实体类
 * @since 2024-01-08 15:11:54
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("system_users")
public class SystemUsers implements Serializable {
    private static final long serialVersionUID = 483944023680073579L;
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 备注
     */
    private String remark;

    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 帐号状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private Timestamp loginDate;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 租户编号
     */
    private String tenantId;




}

