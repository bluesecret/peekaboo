package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserRoleDTO;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUsersView;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * 用户和角色关联表(SystemUserRole)表数据库访问层
 * @since 2024-01-08 11:34:18
 */
@Mapper
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {

    Set<SystemUserRoleDTO> selectUserRoleList(String id);
    Set<String> getUserRoleList(String id);


    List<SystemUserRoleDTO> selectUserRolesById(String id);

    List<SystemUsersView> getBindUser(String id);

}

