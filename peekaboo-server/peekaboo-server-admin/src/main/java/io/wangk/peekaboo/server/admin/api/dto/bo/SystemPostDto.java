package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

/**
 * @Author: bijie
 * @Date: 2024-01-10 11:37
 */
@Data
public class SystemPostDto {
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
     * 当前页
     */
    int currentPage;
    /**
     * 每页显示条数
     */
    int pageSize;

}
