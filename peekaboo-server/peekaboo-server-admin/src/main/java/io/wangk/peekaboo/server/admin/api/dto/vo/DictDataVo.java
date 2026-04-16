package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

/**
 * @author bijie
 * @since 2024/4/7
 */
@Data
public class DictDataVo {
    private String id;
    private String value;
    private String label;
    private Integer status;
    private Integer sort;
}
