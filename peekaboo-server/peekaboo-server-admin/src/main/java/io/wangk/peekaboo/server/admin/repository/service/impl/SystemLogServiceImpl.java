package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemLogVo;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemLogMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bijie
 * @since 2024/4/7
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {
    @Autowired
    private SystemLogMapper systemLogMapper;
    @Override
    public IPage<SystemLog> getPageSystemLogList(SystemLogVo systemLogVo) {
        Page<SystemLog> page = new Page<>(systemLogVo.getCurrentPage(), systemLogVo.getPageSize());
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotBlank(systemLogVo.getNickname()),SystemLog::getNickName,systemLogVo.getNickname())
                .or()
                .like(Strings.isNotBlank(systemLogVo.getNickname()),SystemLog::getUsername,systemLogVo.getNickname())
                .eq(Strings.isNotBlank(systemLogVo.getLogType()),SystemLog::getLogType,systemLogVo.getLogType())

        .orderByDesc(SystemLog::getCreateTime);

        return  systemLogMapper.selectPage(page, wrapper);
    }
}
