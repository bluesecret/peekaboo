package io.wangk.peekaboo.component.satoken.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "sa-token")
@Data
public class SaTokenProperties {
    private List<String> excludePaths = Collections.emptyList();
}
