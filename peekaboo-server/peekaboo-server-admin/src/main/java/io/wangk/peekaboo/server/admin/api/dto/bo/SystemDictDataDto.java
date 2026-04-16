package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bijie
 * @Date: 2024-01-11 16:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDictDataDto {
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
     * 当前页
     */
    int currentPage;
    /**
     * 每页显示条数
     */
    int pageSize;

    private String pid;
}
