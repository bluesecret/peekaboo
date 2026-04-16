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
 * 用户岗位表(SystemUserPost)实体类
 * @since 2024-01-10 16:32:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "system_user_post")
public class SystemUserPost implements Serializable {
    private static final long serialVersionUID = -90169712828878009L;
    /**
     * id
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 岗位ID
     */
    private String postId;
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

