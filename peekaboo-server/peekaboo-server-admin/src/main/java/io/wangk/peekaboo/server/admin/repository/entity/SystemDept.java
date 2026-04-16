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
 * 部门表(SystemDept)实体类
 * @since 2024-01-09 09:08:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("system_dept")
public class SystemDept implements Serializable {
    private static final long serialVersionUID = -56650666499798062L;
    /**
     * 部门id
     */
    private String id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门id
     */
    private String parentId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 负责人
     */
    private String leaderUserId;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 机构编号
     */
    private String code;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    private Integer status;
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

    private String remark;



}

