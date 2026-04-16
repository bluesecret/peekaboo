package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

/**
 * @author bijie
 * @since 2024/3/29
 */
@Data
public class SystemUserDeptVo {
    private String id;
    private String userId;
    private String deptId;
    private String deptName;
}
