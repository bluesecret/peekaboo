package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

@Data
public class SystemUserRoleDTO {
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

    //角色名称
    private String roleName;
    //用户名称
    private String userName;

    private String code;

    private String status;

}
