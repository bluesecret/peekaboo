package io.wangk.peekaboo.component.datapermission.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "data-permission")
public class DataPermissionProperties {

    private boolean enable;

    private Map<String, FieldConfig> includeTables;

    @Data
    public static class FieldConfig {
        private String deptId = "dept_id";
        private String userId = "user_id";
    }

}
