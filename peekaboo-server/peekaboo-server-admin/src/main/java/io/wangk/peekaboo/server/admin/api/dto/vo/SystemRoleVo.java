package io.wangk.peekaboo.server.admin.api.dto.vo;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-08 16:06
 */
@Data
public class SystemRoleVo {
    /**
     * ID
     */
    private String id;
    /**
     * 角色ID
     */
    private String roleId;
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
    private List dataScopeDeptIds;
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
     * 当前角色所对应的菜单
     */
    private List<String> menuIds;
    /**
     * 菜单列表
     */
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;

    private List<SystemMenuVo> systemMenuVoList;

    public String getCreateTime() {
        return DateUtil.formatDateToChinese(createTime);
    }

    public String getUpdateTime() {
        return  DateUtil.formatDateToChinese(updateTime);
    }
}
