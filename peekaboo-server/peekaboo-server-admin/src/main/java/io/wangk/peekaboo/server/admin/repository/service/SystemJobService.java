package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.vo.CatalogMetaDataFetchTaskScheduleVo;
import io.wangk.peekaboo.server.admin.job.schedule.JobScanForClassVo;
import io.wangk.peekaboo.server.admin.job.schedule.JobScheduleCreateOrUpdate;
import io.wangk.peekaboo.server.admin.job.schedule.MapParam;
import io.wangk.peekaboo.server.admin.repository.entity.SystemJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author bijie
 * @since 2024/4/9
 */
public interface SystemJobService extends IService<SystemJob> {
    void createOrUpdate(JobScheduleCreateOrUpdate taskScheduleCreateOrUpdate);

    List<JobScanForClassVo> scanForJobClasses();

    List<String> getCron(MapParam mapParam);

    void deleteById(String id);

    Pages getPageList(CatalogMetaDataFetchTaskScheduleVo data);

    void updateStatus(String id, Integer status) throws SchedulerException;

    void now(String id) throws SchedulerException;
}
