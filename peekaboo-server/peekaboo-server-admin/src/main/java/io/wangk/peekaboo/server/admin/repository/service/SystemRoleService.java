package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemRoleDto;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemRoleMenuDTO;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemRolePermissionDTO;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemMenuView;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemRoleVo;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUsersView;
import io.wangk.peekaboo.server.admin.repository.entity.SystemRole;

import java.util.List;
import java.util.Set;

/**
 * @Author: bijie
 * @Date: 2024-01-08 16:00
 */
public interface SystemRoleService extends IService<SystemRole> {
     Pages getSystemRoleList(SystemRoleDto systemRoleDto);

     void deleteSystemRoleById(String id);

     void addSystemRole(SystemRoleDto systemRoleDto);

     SystemRoleVo getSystemRoleDetails(String id);

     void updateRoleStatus(SystemRoleDto systemRoleDto);

     void updateSystemRole(SystemRoleDto systemRoleDto);

     boolean hasAnySuperAdmin(List<Long> ids);

     List<SystemRoleVo> getRoleList();

     //获取去重角色菜单信息
     List<SystemRoleMenuDTO> getRoleMenuList(List<String> ids);
     //获取角色资源信息
     List<SystemRolePermissionDTO> getRoleMenuListAuthKey(List<String> ids);

     Set<String> getUserRoleList(String id);

    void insertUserRole(SystemRoleDto systemRoleDto);

     void insertRoleMenu(SystemRoleDto systemRoleDto);

     List<SystemUsersView>  getBindUser(String id);

     List<SystemMenuView> getBindMenu(String id);

}
