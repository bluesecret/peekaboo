package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 菜单权限表(SystemMenu)实体类
 * @since 2024-01-09 15:26:30
 */
@Data
public class SystemMenuView implements Serializable {
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
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 是否删除
     */
    private Integer deleted;


    private String remark;


}

