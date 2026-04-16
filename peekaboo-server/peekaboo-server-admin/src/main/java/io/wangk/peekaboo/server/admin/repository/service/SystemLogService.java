package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemLogVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemLog;

/**
 * @author bijie
 * @since 2024/4/7
 */

public interface SystemLogService extends IService<SystemLog> {
    IPage<SystemLog> getPageSystemLogList(SystemLogVo systemLogVo);

}
