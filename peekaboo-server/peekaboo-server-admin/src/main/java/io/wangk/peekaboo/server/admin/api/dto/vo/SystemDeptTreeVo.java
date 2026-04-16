package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

import java.util.List;

/**
 * @author bijie
 * @since 2024/3/28
 */
@Data
public class SystemDeptTreeVo {
    private String id;
    private String parentId;
    private String value;
    private String label;
    private List<SystemDeptTreeVo> children;
}
