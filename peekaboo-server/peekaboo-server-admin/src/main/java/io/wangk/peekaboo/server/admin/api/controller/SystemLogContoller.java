package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemLogVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemLogService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author bijie
 * @since 2024/4/7
 */
@RestController
@Slf4j
@RefreshToken
@RequestMapping("/log")
public class SystemLogContoller {

    @Autowired
    private SystemLogService systemLogService;

    /**
     * 获取日志列表分页
     */
    @PostMapping("/getPageSystemLogList")
    @Operation(summary = "获取日志列表分页")
    @Record(value = "获取日志列表分页",label = LabelType.QUERY)
    public Pages getPageSystemLogList(@RequestBody SystemLogVo systemLogVo) {
        IPage<SystemLog> pageSystemLogList = systemLogService.getPageSystemLogList(systemLogVo);
        List<SystemLog> records = pageSystemLogList.getRecords();
        List<SystemLogVo> systemLogVos = BeanUtils.toBeanList(records, SystemLogVo.class);
        return new Pages(systemLogVos ,pageSystemLogList.getTotal());
    }


    @GetMapping("/delete")
    @Operation(summary = "删除日志")
    @Record(value = "删除日志",label = LabelType.DELETE)
    public void delete(@RequestParam("id") String id) {
        systemLogService.removeBatchByIds(Arrays.asList(id.split(",").clone()));
    }

    @GetMapping("findById")
    @Operation(summary = "根据id查询日志")
    @Record(value = "根据id查询日志",label = LabelType.QUERY)
    public SystemLog findById(@RequestParam("id") String id) {
        return systemLogService.getById(id);
    }
}
