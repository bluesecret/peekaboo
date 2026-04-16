package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bijie
 * @Date: 2024-01-11 16:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDictTypeDto {
    /**
     * 字典主键
     */
    private String id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典类型
     */
    private String type;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
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



}
