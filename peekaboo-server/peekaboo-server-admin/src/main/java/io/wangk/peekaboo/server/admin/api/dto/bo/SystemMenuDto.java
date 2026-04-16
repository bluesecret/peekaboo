package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bijie
 * @Date: 2024-01-09 11:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemMenuDto {
    /**
     * 菜单ID
     */
    private String id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 父菜单ID
     */
    private String parentId;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 组件名
     */
    private String componentName;
    /**
     * 菜单状态
     */
    private Integer status;
    /**
     * 是否可见
     */
    private Integer visible;
    /**
     * 是否缓存
     */
    private Integer keepAlive;
    /**
     * 是否总是显示
     */
    private Integer alwaysShow;


    private String remark;
}
