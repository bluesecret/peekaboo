package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUserPostVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUserPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户岗位表(SystemUserPost)表数据库访问层
 * @since 2024-01-10 16:32:47
 */
@Mapper
public interface SystemUserPostMapper extends BaseMapper<SystemUserPost> {


    List<SystemUserPostVo> getUserPostListByUserId(String id);

}

