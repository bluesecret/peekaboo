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
 * 岗位信息表(SystemPost)实体类
 * @since 2024-01-10 07:41:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "system_post")
public class SystemPost implements Serializable {
    private static final long serialVersionUID = 316886276151004317L;
    /**
     * 岗位ID
     */
    private String id;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
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
    private Long tenantId;


    public Timestamp getCreateTime() {
        return createTime;
    }



    public Timestamp getUpdateTime() {
        return updateTime;
    }


}

