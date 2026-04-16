package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.repository.entity.SystemJobLog;

/**
 * @author bijie
 * @since 2024/4/11
 */
public interface SystemJobLogService extends IService<SystemJobLog> {
   Pages getJobDetil(String id,Integer pageSize,Integer pageNum);
}
