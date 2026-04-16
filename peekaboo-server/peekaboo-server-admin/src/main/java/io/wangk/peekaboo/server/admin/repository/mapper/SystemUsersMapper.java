package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUserVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息表(SystemUsers)表数据库访问层
 * @since 2024-01-08 10:14:19
 */
@Mapper
public interface SystemUsersMapper extends BaseMapper<SystemUsers> {

    SystemUserDto selectByUsername(String username);

    List<SystemUserVo>getUserListByDept(@Param("systemUserDto") SystemUserDto systemUserDto);


}

