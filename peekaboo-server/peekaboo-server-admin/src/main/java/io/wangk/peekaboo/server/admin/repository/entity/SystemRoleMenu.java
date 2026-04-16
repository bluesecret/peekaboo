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
 * 角色和菜单关联表(SystemRoleMenu)实体类
 * @since 2024-01-08 17:15:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "system_role_menu")
public class SystemRoleMenu implements Serializable {
    private static final long serialVersionUID = -23630218520607842L;
    /**
     * 自增编号
     */
    private String id;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 菜单ID
     */
    private String menuId;
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
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 租户编号
     */
    private String tenantId;

    private String status;


}

