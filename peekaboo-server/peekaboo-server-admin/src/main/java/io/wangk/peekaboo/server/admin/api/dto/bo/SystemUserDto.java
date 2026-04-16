package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-08 10:42
 */
@Data
public class SystemUserDto {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 帐号状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;

    /**
     * 角色列表
     */
    private List<String> roleIds;


    private String deptId;

    private List<String> deptIds;
    /**
     * 当前页
     */
    int currentPage;
    /**
     * 每页显示条数
     */
    int pageSize;
    /**
     * 用户岗位
     */
    private List<String> postIds;


    private List<String> userIds;

    /**
     * 备注
     */
    private String remark;

    private String avatar;

    private String deptName;

    private String createTime;

    private String updateTime;
}
