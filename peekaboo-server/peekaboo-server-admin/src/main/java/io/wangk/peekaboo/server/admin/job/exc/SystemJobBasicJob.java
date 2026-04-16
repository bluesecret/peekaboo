package io.wangk.peekaboo.server.admin.job.exc;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemJob;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemJobLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobLogService;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bijie
 * @since 2024/4/11
 */
@Slf4j
public abstract class SystemJobBasicJob implements Job {


    @Autowired
    private SystemJobLogService systemJobLogService;


    @Autowired
    private SystemJobService systemJobService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long startTime = System.currentTimeMillis();
        String dateTimeStr = DateUtils.getDateTimeStr();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String id = (String) dataMap.get("job_id");
        SystemJob systemJob = systemJobService.getById(id);
        boolean doExecute = true;
        String message = "执行成功";
        try {
            doExecute(jobExecutionContext);
        }catch (Exception e){
            doExecute = false;
            message = e.getMessage();
            e.printStackTrace();
            log.info(message);
        }

        long endTime = System.currentTimeMillis();
        //记录日志
        SystemJobLog systemJobLog = SystemJobLog.builder().id(IdUtils.create()).jobId(id).className(systemJob.getClassName()).name(systemJob.getName())
                .cons(endTime - startTime).startTime(dateTimeStr).endTime(DateUtils.getDateTimeStr())
                .result(doExecute ? "success" : "error").message(message).build();
        systemJobLogService.save(systemJobLog);
    }

    public abstract void doExecute(JobExecutionContext jobExecutionContext) throws RuntimeException;
}
