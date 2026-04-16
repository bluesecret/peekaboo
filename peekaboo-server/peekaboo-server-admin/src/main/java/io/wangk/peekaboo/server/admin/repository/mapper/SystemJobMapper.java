package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.repository.entity.SystemJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author bijie
 * @since 2024/4/9
 */
@Mapper
public interface SystemJobMapper extends BaseMapper<SystemJob> {
    @Select("SELECT * from dv_catalog_metadata_fetch_task_schedule WHERE datasource_id = #{datasourceId} limit 1")
    SystemJob getByDataSourceId(String datasourceId);
}
