package io.wangk.peekaboo.server.admin.job.schedule;

import lombok.Builder;
import lombok.Data;

/**
 * @author bijie
 * @since 2024/4/15
 */
@Builder
@Data
public class JobScanForClassVo {
    //类名
    private String className;
    //描述
    private String description;
}
