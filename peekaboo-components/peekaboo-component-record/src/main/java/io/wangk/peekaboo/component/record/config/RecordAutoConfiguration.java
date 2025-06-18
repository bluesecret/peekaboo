package io.wangk.peekaboo.component.record.config;

import io.wangk.peekaboo.component.record.aspect.RecordAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(value = "ruixi.component.record.enabled", matchIfMissing = true, havingValue = "true")
public class RecordAutoConfiguration {

    @Bean
    public RecordAspect recordAspect() {
        return new RecordAspect();
    }
}
