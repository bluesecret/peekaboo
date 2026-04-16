package io.wangk.peekaboo.server.admin.api.dto.vo;


import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-09 11:02
 */
@Data
public class SystemMenuVo {
    /**
     * 菜单ID
     */
    private String id;

    private String key;
    private String title;
    private String value;
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
     * 子菜单集合
     */
    List<SystemMenuVo> children;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;


    private String remark;
    private List<SystemMenuVo> systemMenuVoList;

    public String getCreateTime() {
        return DateUtil.formatDateToChinese(createTime);
    }

    public String getUpdateTime() {
        return  DateUtil.formatDateToChinese(updateTime);
    }



}
