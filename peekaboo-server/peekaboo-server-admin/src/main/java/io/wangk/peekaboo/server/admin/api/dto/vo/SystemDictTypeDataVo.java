package io.wangk.peekaboo.server.admin.api.dto.vo;

import lombok.Data;

import java.util.List;

/**
 * @author bijie
 * @since 2024/4/7
 */
@Data
public class SystemDictTypeDataVo {
    private String id;
    private String name;
    private String type;
    private List<DictDataVo> dictDataList;
}
