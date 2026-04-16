package io.wangk.peekaboo.server.admin.repository.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户和角色关联表(SystemUserRole)实体类
 * @since 2024-01-08 11:29:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "system_user_role")
public class SystemUserRole implements Serializable {
    private static final long serialVersionUID = 264336014065361634L;
    /**
     * 自增编号
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 状态
     */
    private String status;
    /**
     * 租户编号
     */
    private String tenantId;

}

