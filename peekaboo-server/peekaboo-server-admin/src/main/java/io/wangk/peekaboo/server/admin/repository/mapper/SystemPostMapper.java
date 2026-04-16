package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.repository.entity.SystemPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 岗位信息表(SystemPost)表数据库访问层
 * @since 2024-01-10 07:41:32
 */
@Mapper
public interface SystemPostMapper extends BaseMapper<SystemPost> {
}

