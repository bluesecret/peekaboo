package io.wangk.peekaboo.server.admin.api.dto.vo;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-10 09:28
 */
@Data
public class SystemPostVo {
    /**
     * 岗位ID
     */
    private String id;
    /**
     * 岗位编码
     */
    private String code;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
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

    public String getCreateTime() {
        return DateUtil.formatDateToChinese(createTime);
    }

    public String getUpdateTime() {
        return  DateUtil.formatDateToChinese(updateTime);
    }
}
