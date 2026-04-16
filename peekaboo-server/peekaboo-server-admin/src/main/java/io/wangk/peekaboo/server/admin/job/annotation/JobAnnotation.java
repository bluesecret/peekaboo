package io.wangk.peekaboo.server.admin.job.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author bijie
 * @since 2024/4/15
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface JobAnnotation {
    String value();
}