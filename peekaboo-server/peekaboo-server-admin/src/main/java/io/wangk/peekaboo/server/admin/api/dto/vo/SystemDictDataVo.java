package io.wangk.peekaboo.server.admin.api.dto.vo;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-11 16:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDictDataVo {
    /**
     * 字典编码
     */
    private String id;
    /**
     * 字典排序
     */
    private Integer sort;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典键值
     */
    private String value;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 颜色类型
     */
    private String colorType;
    /**
     * css 样式
     */
    private String cssClass;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;

    private List<SystemMenuVo> systemMenuVoList;

    private String pid;

    private List<SystemDictDataVo> children;

    public String getCreateTime() {
        return DateUtil.formatDateToChinese(createTime);
    }

    public String getUpdateTime() {
        return  DateUtil.formatDateToChinese(updateTime);
    }


}
