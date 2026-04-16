package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemJobLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemJobLogMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bijie
 * @since 2024/4/11
 */
@Service
public class SystemJobLogServiceImpl extends ServiceImpl<SystemJobLogMapper, SystemJobLog> implements SystemJobLogService {

    @Autowired
    private SystemJobLogMapper jobLogMapper;
    @Override
    public Pages getJobDetil(String id, Integer pageSize, Integer currentPage) {

        Page<SystemJobLog> page = new Page<>(currentPage, pageSize);
        QueryWrapper<SystemJobLog> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",id);
        Page<SystemJobLog> selectPage = jobLogMapper.selectPage(page, wrapper);
        return new Pages(selectPage.getRecords(),selectPage.getTotal());
    }
}
