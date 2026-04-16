package io.wangk.peekaboo.server.admin.api.dto.vo;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-04 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDeptVo {

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
     * 部门状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 负责人
     */
    private String leaderUserId;
    /**
     *子模块集合
     */
    private List<SystemDeptVo> children;
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
    private String key;

    private String title;

    private String value;

}
