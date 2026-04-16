package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

import java.util.List;

/**
 * @author bijie
 * @since 2024/3/28
 */
@Data
public class SystemDeptTreeDetailVo {
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
    private String leaderUserName;
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

    private List<SystemDeptTreeDetailVo> children;

    private int pageSize;

    private int currentPage;
}
