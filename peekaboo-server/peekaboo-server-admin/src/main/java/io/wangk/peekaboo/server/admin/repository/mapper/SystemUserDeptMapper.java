package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUserDeptVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUserDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门表(SystemDept)表数据库访问层
 * @since 2024-01-09 09:08:20
 */
@Mapper
public interface SystemUserDeptMapper extends BaseMapper<SystemUserDept> {

    List<SystemUserDeptVo> getUserDeptListByUserId(String id);
}

