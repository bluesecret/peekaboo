package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.CatalogMetaDataFetchTaskScheduleVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.Status;
import cn.ruixi.azure.rainbow.boot.module.system.exception.DataFlexServerException;
import cn.ruixi.azure.rainbow.boot.module.system.job.annotation.JobAnnotation;
import cn.ruixi.azure.rainbow.boot.module.system.job.schedule.*;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.QuartzExecutors;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.cron.FunCron;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.cron.StrategyFactory;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemJob;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemJobMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemJobService;
import cn.ruixi.azure.rainbow.boot.security.core.util.SecurityFrameworkUtils;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bijie
 * @since 2024/4/9
 */
@Service
@Slf4j
public class SystemJobServiceImpl extends ServiceImpl<SystemJobMapper, SystemJob> implements SystemJobService, ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Autowired
    private QuartzExecutors quartzExecutors;

    @Autowired
    private SystemJobMapper mapper;


    @Autowired
    private Scheduler scheduler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrUpdate(JobScheduleCreateOrUpdate taskScheduleCreateOrUpdate) {
        if (Strings.isNotBlank(taskScheduleCreateOrUpdate.getId())) {
            update(taskScheduleCreateOrUpdate);
        } else {
            create(taskScheduleCreateOrUpdate);
        }

    }

    @Override
    public List<JobScanForClassVo> scanForJobClasses() {
        List<JobScanForClassVo> list = new ArrayList<>();
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(JobAnnotation.class);
        for (String value : beanNamesForAnnotation) {
            Class<?> clazz = applicationContext.getType(value);
            JobAnnotation annotation = clazz.getAnnotation(JobAnnotation.class);
            list.add(JobScanForClassVo.builder().className(clazz.getName()).description(annotation.value()).build());
        }
        return list;
    }

    @Override
    public List<String> getCron(MapParam mapParam) {
        List<String> listCron = new ArrayList<String>();
        FunCron api = StrategyFactory.getByType(mapParam.getCycle());
        JobSchedule jobSchedule = new JobSchedule();
        String result1 = JSON.toJSONString(mapParam);
        jobSchedule.setParam(result1);
        String cron = api.funcDeal(result1);
        listCron.add(cron);
        return listCron;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        if (Strings.isBlank(id)) {
            throw new BusinessException("id不能为空");
        }
        String[] strs = id.split(",");
        for (String str : strs) {
            SystemJob systemJob = mapper.selectById(str);
            if (systemJob != null) {
                quartzExecutors.deleteJob(getScheduleJobInfo(systemJob));
                mapper.deleteById(str);
            }
        }

    }

    @Override
    public Pages getPageList(CatalogMetaDataFetchTaskScheduleVo data) {
        Page<SystemJob> page = new Page<>(data.getCurrentPage(), data.getPageSize());
        LambdaQueryWrapper<SystemJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strings.isNotBlank(data.getType()), SystemJob::getType, data.getType())
                .like(Strings.isNotBlank(data.getName()), SystemJob::getName, data.getName())
                .orderByDesc(SystemJob::getCreateTime);
        Page<SystemJob> selectPage = mapper.selectPage(page, wrapper);
        List<SystemJob> records = selectPage.getRecords();

        return new Pages(records, selectPage.getTotal());
    }

    @Override
    public void updateStatus(String id, Integer status) throws SchedulerException {
        if (status == CommonStatusEnum.ENABLE.getStatus()) {
            scheduler.resumeJob(new JobKey("CATALOG_JOB_" + id, "CATALOG_JOB_GROUP_" + id));
        } else {
            scheduler.pauseJob(new JobKey("CATALOG_JOB_" + id, "CATALOG_JOB_GROUP_" + id));
        }
        SystemJob schedule = new SystemJob();
        schedule.setId(id);
        schedule.setStatus(status);
        mapper.updateById(schedule);
    }

    @Override
    public void now(String id) throws SchedulerException {
        scheduler.triggerJob(new JobKey("CATALOG_JOB_" + id, "CATALOG_JOB_GROUP_" + id));
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("basedir"));
    }

    private void updateJobScheduleParam(SystemJob jobSchedule, String type) {
        switch (type) {
            case "3":
                if (Strings.isBlank(jobSchedule.getCronExpression())) {
                    throw new DataFlexServerException(Status.SCHEDULE_PARAMETER_IS_NULL_ERROR);
                }

                Boolean isValid = quartzExecutors.isValid(jobSchedule.getCronExpression());
                if (!isValid) {
                    throw new DataFlexServerException(Status.SCHEDULE_CRON_IS_INVALID_ERROR, jobSchedule.getCronExpression());
                }

                break;
            case "1":
                break;
            default:
                throw new DataFlexServerException(Status.SCHEDULE_TYPE_NOT_VALIDATE_ERROR, type);
        }
    }

    private void addScheduleJob(JobScheduleCreateOrUpdate jobScheduleCreate, SystemJob systemJob) throws ParseException, ClassNotFoundException {
        switch (jobScheduleCreate.getType()) {
            case "2":
            case "3":
                quartzExecutors.addJob(Class.forName(jobScheduleCreate.getClassName()).asSubclass(Job.class), getScheduleJobInfo(systemJob));
                break;
            case "1":
                break;
            default:
                throw new DataFlexServerException(Status.SCHEDULE_TYPE_NOT_VALIDATE_ERROR, jobScheduleCreate.getType());
        }
    }

    private ScheduleJobInfo getScheduleJobInfo(SystemJob systemJob) {
        return new ScheduleJobInfo(
                ScheduleJobType.CATALOG,
                systemJob.getId(),
                systemJob.getCronExpression(),
                systemJob.getStartTime(),
                systemJob.getEndTime());
    }

    private void create(JobScheduleCreateOrUpdate jobScheduleCreate) {

        String username = SecurityFrameworkUtils.getRainbowUsername();
        SystemJob systemJob = new SystemJob();
        BeanUtils.copyProperties(jobScheduleCreate, systemJob);
        systemJob.setCreateBy(username);
        systemJob.setCreateTime(LocalDateTime.now());
        systemJob.setUpdateBy(username);
        systemJob.setUpdateTime(LocalDateTime.now());
        systemJob.setStatus(CommonStatusEnum.ENABLE.getStatus());
        systemJob.setId(IdUtils.create());
        updateJobScheduleParam(systemJob, jobScheduleCreate.getType());
        systemJob.setCronExpression(systemJob.getCronExpression());
        if (baseMapper.insert(systemJob) <= 0) {
            log.info("create catalog task schedule fail : {}", systemJob);
            throw new DataFlexServerException(Status.CREATE_CATALOG_TASK_SCHEDULE_ERROR);
        }
        try {
            addScheduleJob(jobScheduleCreate, systemJob);
        } catch (Exception e) {
            throw new DataFlexServerException(Status.ADD_QUARTZ_ERROR, e);
        }

        log.info("create job schedule success: datasource id : {}, cronExpression : {}",
                systemJob.getClassName(),
                systemJob.getCronExpression());
    }

    private SystemJob update(JobScheduleCreateOrUpdate jobScheduleUpdate) throws DataFlexServerException {

        String username = SecurityFrameworkUtils.getRainbowUsername();

        SystemJob catalogTaskScheduleJob = new SystemJob();
        BeanUtils.copyProperties(jobScheduleUpdate, catalogTaskScheduleJob);
        catalogTaskScheduleJob.setUpdateBy(username);
        catalogTaskScheduleJob.setUpdateTime(LocalDateTime.now());
        updateJobScheduleParam(catalogTaskScheduleJob, jobScheduleUpdate.getType());


        try {
            ScheduleJobInfo scheduleJobInfo = getScheduleJobInfo(catalogTaskScheduleJob);
            quartzExecutors.deleteJob(scheduleJobInfo);
            addScheduleJob(jobScheduleUpdate, catalogTaskScheduleJob);
        } catch (Exception e) {
            throw new DataFlexServerException(Status.ADD_QUARTZ_ERROR, e.getMessage());
        }


        if (baseMapper.updateById(catalogTaskScheduleJob) <= 0) {
            log.info("update job schedule fail : {}", jobScheduleUpdate);
            throw new DataFlexServerException(Status.UPDATE_JOB_SCHEDULE_ERROR, jobScheduleUpdate.getId());
        }

        return catalogTaskScheduleJob;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
