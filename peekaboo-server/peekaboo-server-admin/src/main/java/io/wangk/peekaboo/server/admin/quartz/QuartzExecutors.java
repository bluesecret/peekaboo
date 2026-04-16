package io.wangk.peekaboo.server.admin.quartz;

import cn.ruixi.azure.rainbow.boot.module.system.job.schedule.ScheduleJobInfo;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.constant.DataFlexConstants;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Service
public class QuartzExecutors {
    public  final  static String DEAULT_GTOUP_NAME = "DEFAULT_GROUP";
    @Autowired
    private Scheduler scheduler;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private QuartzExecutors() {
    }

    /**
     * add task trigger , if this task already exists, return this task with updated trigger
     *
     * @param clazz job class name
     * @param schedule schedule job info
     * @param schedule schedule
     */
    public void addJob(Class<? extends Job> clazz, final ScheduleJobInfo schedule) throws ParseException {
        String jobGroupName = buildJobGroupName(schedule);
        String jobName = buildJobName(schedule);

        Map<String, Object> jobDataMap = buildDataMap(schedule);
        String cronExpression = schedule.getCronExpression();

        /*
         * transform from server default timezone to schedule timezone
         * e.g. server default timezone is `UTC`
         * user set a schedule with startTime `2022-04-28 10:00:00`, timezone is `Asia/Shanghai`,
         * api skip to transform it and save into databases directly, startTime `2022-04-28 10:00:00`, timezone is `UTC`, which actually added 8 hours,
         * so when add job to quartz, it should recover by transform timezone
         */

        Date startDate = buildDate(schedule.getStartTime());
        Date endDate = buildDate(schedule.getEndTime());

        lock.writeLock().lock();
        try {

            JobKey jobKey = new JobKey(jobName, jobGroupName);
            JobDetail jobDetail;
            //add a task (if this task already exists, return this task directly)
            if (scheduler.checkExists(jobKey)) {
                jobDetail = scheduler.getJobDetail(jobKey);
                jobDetail.getJobDataMap().putAll(jobDataMap);
            } else {
                jobDetail = newJob(clazz).withIdentity(jobKey).build();
                jobDetail.getJobDataMap().putAll(jobDataMap);
                scheduler.addJob(jobDetail, false, true);
                log.info("Add job, job name: {}, group name: {}", jobName, jobGroupName);
            }

            TriggerKey triggerKey = new TriggerKey(jobName, jobGroupName);

            /*
             * Instructs the Scheduler that upon a mis-fire
             * situation, the CronTrigger wants to have it's
             * next-fire-time updated to the next time in the schedule after the
             * current time (taking into account any associated Calendar),
             * but it does not want to be fired now.
             */
            CronTrigger cronTrigger = newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(startDate)
                    .endAt(endDate)
                    .withSchedule(
                            cronSchedule(cronExpression)
                                    .withMisfireHandlingInstructionDoNothing())
                    .forJob(jobDetail).build();

            if (scheduler.checkExists(triggerKey)) {
                // updateProcessInstance scheduler trigger when scheduler cycle changes
                CronTrigger oldCronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                String oldCronExpression = oldCronTrigger.getCronExpression();

                if (!StringUtils.equalsIgnoreCase(cronExpression, oldCronExpression)) {
                    // reschedule job trigger
                    scheduler.rescheduleJob(triggerKey, cronTrigger);
                    log.info("reschedule job trigger, triggerName: {}, triggerGroupName: {}, cronExpression: {}, startDate: {}, endDate: {}",
                            DEAULT_GTOUP_NAME, jobGroupName, cronExpression, startDate, endDate);
                }
            } else {
                scheduler.scheduleJob(cronTrigger);
                log.info("schedule job trigger, triggerName: {}, triggerGroupName: {}, cronExpression: {}, startDate: {}, endDate: {}",
                        jobName, jobGroupName, cronExpression, startDate, endDate);
            }
        } catch (Exception e) {
            log.error("add job failed", e);
            throw new RuntimeException("add job failed", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * delete job
     *
     * @param schedule ScheduleJobInfo
     * @return true if the Job was found and deleted.
     */
    public boolean deleteJob(ScheduleJobInfo schedule) {
        lock.writeLock().lock(); String jobName = buildJobName(schedule);
        String jobGroupName = buildJobGroupName(schedule);
        JobKey jobKey = new JobKey(jobName,jobGroupName);
        try {
            if(scheduler.checkExists(jobKey)){
                log.info("try to delete job, job name: {}, job group name: {},", jobName, jobGroupName);
                return scheduler.deleteJob(jobKey);
            } else {
                return true;
            }
        } catch (SchedulerException e) {
            log.error("delete job : {} failed", jobName, e);
        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    /**
     * delete all jobs in job group
     *
     * @param jobGroupName job group name
     *
     * @return true if all of the Jobs were found and deleted, false if
     *      one or more were not deleted.
     */
    public boolean deleteAllJobs(Scheduler scheduler, String jobGroupName) {
        lock.writeLock().lock();
        try {
            log.info("try to delete all jobs in job group: {}", jobGroupName);
            List<JobKey> jobKeys =
                    new ArrayList<>(scheduler.getJobKeys(GroupMatcher.groupEndsWith(jobGroupName)));
            return scheduler.deleteJobs(jobKeys);
        } catch (SchedulerException e) {
            log.error("delete all jobs in job group: {} failed",jobGroupName, e);
        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    /**
     * build job name
     * @param schedule ScheduleJobInfo
     * @return job name
     */
    private static String buildJobName(ScheduleJobInfo schedule) {
        return schedule.getType().getDescription() + "_JOB" + DataFlexConstants.UNDERLINE + schedule.getId();
    }

    /**
     * build job group name
     * @param schedule ScheduleJobInfo
     * @return job group name
     */
    private static String buildJobGroupName(ScheduleJobInfo schedule) {
        return schedule.getType().getDescription() + "_JOB_GROUP" + DataFlexConstants.UNDERLINE + schedule.getId() ;
    }

    /**
     * @param schedule  schedule
     * @return map
     */
    private static Map<String, Object> buildDataMap( ScheduleJobInfo schedule) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put(DataFlexConstants.JOB_ID, schedule.getId());
        dataMap.put(DataFlexConstants.SCHEDULE, JSON.toJSONString(schedule));
        return dataMap;
    }

    private static Date buildDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public  boolean isValid(String cronExpression){
        return CronExpression.isValidExpression(cronExpression);
    }
}
