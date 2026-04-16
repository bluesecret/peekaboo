package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-08 16:32
 */
@Data
public class SystemRoleDto {
    /**
     * 角色id
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
    private List<String> dataScopeDeptIds;
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
     * 当前页
     */
    int currentPage;
    /**
     * 每页显示条数
     */
    int pageSize;

    /**
     * 菜单列表
     */
    private List<String> menuList;

    /**
     * 用户列表
     */
    private List<String> userList;
}
