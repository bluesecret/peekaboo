package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemRoleMenuDTO;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemRolePermissionDTO;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemMenuView;
import io.wangk.peekaboo.server.admin.repository.entity.SystemRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色和菜单关联表(SystemRoleMenu)表数据库访问层
 * @since 2024-01-08 17:15:12
 */
@Mapper
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {

    //根据角色数组id查询菜单数组
    List<SystemRoleMenuDTO> selectRoleMenuList(List<String> roleIds);

    //根据角色数组获取资源信息
    List<SystemRolePermissionDTO> selectRoleMenuPermission(List<String> roleIds);


    List<SystemMenuView> getBindMenu(String id);

}

