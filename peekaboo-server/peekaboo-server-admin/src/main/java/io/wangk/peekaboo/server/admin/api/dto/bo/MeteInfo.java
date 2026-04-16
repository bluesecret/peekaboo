package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

/**
 * @author bijie
 * @since 2024/3/25
 */
@Data
public class MeteInfo {
    private String type;
    private String icon;
    private String name;
    private String visible;
    private String keepAlive;
}
