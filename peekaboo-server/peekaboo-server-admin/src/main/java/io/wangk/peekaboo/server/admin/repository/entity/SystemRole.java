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
 * 角色信息表(SystemRole)实体类
 * @since 2024-01-08 15:43:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "system_role")
public class SystemRole implements Serializable {
    private static final long serialVersionUID = -98812786198906275L;
    /**
     * 角色ID
     */
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色权限字符串
     */
    private String code;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;
    /**
     * 数据范围(指定部门数组)
     */
    private String dataScopeDeptIds;
    /**
     * 角色状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 角色类型
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;
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

}

