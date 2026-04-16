package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bijie
 * @Date: 2024-01-04 11:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDeptDto {

    /**
     * 部门id
     */
    private String id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码
     */
    private String code;
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
     * 负责人名称
     */
    private String leaderName;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    private Integer status;


    private Integer pageSize;

    private Integer currentPage;

    private String remark;
}
