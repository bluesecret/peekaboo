package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.CatalogMetaDataFetchTaskScheduleVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.job.schedule.JobScanForClassVo;
import cn.ruixi.azure.rainbow.boot.module.system.job.schedule.JobScheduleCreateOrUpdate;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobLogService;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author bijie
 * @since 2024/4/9
 */

@RestController
@Slf4j
@RefreshToken
@RequestMapping("/job")
public class SystemJobController {


    @Autowired
    private SystemJobService jobService;
    @Autowired
    private SystemJobLogService jobLogService;


    @Operation(summary = "create or update catalog metadata fetch task schedule")
    @PostMapping(value = "/createOrUpdate",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Record(value = "创建/修改定时任务信息",label = LabelType.INSERT)
    public void createOrUpdateJob(@Valid @RequestBody JobScheduleCreateOrUpdate jobScheduleCreateOrUpdate)  {
         jobService.createOrUpdate(jobScheduleCreateOrUpdate);
    }


    @GetMapping("/scanForJobClasses")
    @Operation(summary = "scan for job classes")
    @Record(value = "获取项目中定时任务类",label = LabelType.QUERY,result = true)
    public List<JobScanForClassVo> scanForJobClasses() {
        return jobService.scanForJobClasses();
    }


    @Operation(summary = "get job schedule by id")
    @GetMapping(value = "/findById")
    @Record(value = "查询定时任务详情",label = LabelType.QUERY)
    public Object findById(@RequestParam String id)  {
        return jobService.getById(id);
    }



    @Operation(summary = "delete by id")
    @GetMapping(value = "/deleteById")
    @Record(value = "删除定时任务信息",label = LabelType.DELETE)
    public void deleteById(@RequestParam String id)  {
         jobService.deleteById(id);
    }


    @Operation(summary = "分页查询")
    @PostMapping("/getPageList")
    @Record(value = "删除定时分页查询任务信息",label = LabelType.QUERY)
    public Object getPageList(@RequestBody CatalogMetaDataFetchTaskScheduleVo data) {
        return jobService.getPageList(data);
    }


    @Operation(summary = "修改状态")
    @GetMapping("updateStatus")
    @Record(value = "修改状态",label = LabelType.UPDATE)
    public void updateStatus(@RequestParam String id ,@RequestParam Integer status) throws SchedulerException {
        jobService.updateStatus(id,status);
    }


    @GetMapping("/getJobDetil")
    @Operation(summary = "获取定时任务执行日志")
    @Record(value = "获取定时任务执行日志",label = LabelType.QUERY)
    public Object getJobDetil(@RequestParam String id,
                              @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                              @RequestParam(value = "currentPage",required = false,defaultValue = "1")Integer currentPage) {
        return jobLogService.getJobDetil(id,pageSize,currentPage);
    }

    @GetMapping("/now")
    @Operation(summary = "立即执行")
    @Record(value = "立即执行",label = LabelType.QUERY)
    public void now(@RequestParam String id) throws SchedulerException {
        jobService.now(id);
    }
}
