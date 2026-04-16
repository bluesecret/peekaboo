package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRoleDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuView;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemRoleVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemUsersView;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemRoleService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-08 15:56
 */
@RestController
@Slf4j
@RequestMapping("/role")
@RefreshToken
public class SystemRoleController {

    @Resource
    private SystemRoleService systemRoleService;

    /**
     * 获取角色列表
     * @param systemRoleDto
     * @return
     */
   // @PreAuthorize("@ps.hasPermission('system:role:query')")
    @PostMapping("/getSystemRoleList")
    @Operation(summary = "获取角色列表（分页）")
    @Record(value = "获取角色列表（分页）",label = "Query")
    public Pages systemRoleList(@RequestBody SystemRoleDto systemRoleDto){
       Pages pages= systemRoleService.getSystemRoleList(systemRoleDto);
        return pages;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
  //  @PreAuthorize("@ps.hasPermission('system:role:delete')")
    @GetMapping("/deleteSystemRoleById")
    @Operation(summary = "删除角色信息")
    @Record(value = "删除角色信息",label = "Delete")
    public void deleteSystemRoleById(@RequestParam("id") String id){
        systemRoleService.deleteSystemRoleById(id);
    }

    /**
     * 添加角色
     * @param systemRoleDto
     * @return
     */
   // @PreAuthorize("@ps.hasPermission('system:role:create')")
    @PostMapping("/addSystemRole")
    @Operation(summary = "新增角色")
    @Record(value = "新增角色",label = LabelType.CREATE)
    public void addSystemRole(@RequestBody SystemRoleDto systemRoleDto){
        systemRoleService.addSystemRole(systemRoleDto);
    }

    /**
     * 获取角色信息
     */
  //  @PreAuthorize("@ps.hasPermission('system:role:query')")
    @GetMapping("/getSystemRoleDetails")
    @Operation(summary = "获取角色信息")
    @Record(value = "获取角色信息",label = LabelType.QUERY)
    public SystemRoleVo getSystemRoleDetails(@RequestParam("id")String id){
        SystemRoleVo systemRoleVo = systemRoleService.getSystemRoleDetails(id);
        return systemRoleVo;
    }

    /**
     * 修改角色状态
     */
   // @PreAuthorize("@ps.hasPermission('system:role:update')")
    @PostMapping("/updateRoleStatus")
    @Operation(summary = "修改角色状态")
    @Record(value = "修改角色状态",label = LabelType.UPDATE)
    public void updateRoleStatus(@RequestBody SystemRoleDto systemRoleDto){
        systemRoleService.updateRoleStatus(systemRoleDto);
    }

    /**
     * 修改角色信息
     */
  //  @PreAuthorize("@ps.hasPermission('system:role:update')")
    @PostMapping("/updateSystemRole")
    @Operation(summary = "修改角色信息")
    @Record(value = "修改角色信息",label =LabelType.UPDATE)
    public void updateSystemRole(@RequestBody SystemRoleDto systemRoleDto){
        systemRoleService.updateSystemRole(systemRoleDto);
    }

    /**
     * 获取角色列表
     */
    @GetMapping("/getRoleList")
    @Operation(summary = "获取角色列表（不分页）")
    @Record(value = "获取角色列表（不分页）",label = LabelType.QUERY)
    public List<SystemRoleVo> getRoleList(){
        List<SystemRoleVo> list=  systemRoleService.getRoleList();
        return list;
    }

    /**
     * 角色绑定用户
     */
    @PostMapping("/insertUserRole")
    @Operation(summary = "角色绑定用户")
    @Record(value = "角色绑定用户",label = LabelType.INSERT)
    public void insertUserRole(@RequestBody SystemRoleDto systemRoleDto){
        systemRoleService.insertUserRole(systemRoleDto);

    }

    /**
     * 角色绑定菜单
     */
    @PostMapping("/insertRoleMenu")
    @Operation(summary = "角色绑定菜单")
    @Record(value = "角色绑定菜单",label = LabelType.INSERT)
    public void insertRoleMenu(@RequestBody SystemRoleDto systemRoleDto){
        systemRoleService.insertRoleMenu(systemRoleDto);
    }

    /**
     * 获取已经绑定的角色用户
     */
    @GetMapping("/getBindUser")
    @Operation(summary = "获取已经绑定的角色用户")
    @Record(value = "获取已经绑定的角色用户",label = LabelType.QUERY)
    public List<SystemUsersView>  getBindUser(@RequestParam("id")String id){
        return systemRoleService.getBindUser(id);

    }

    /**
     * 获取角色绑定的菜单
     * @param id
     * @return
     */
    @GetMapping("/getBindMenu")
    @Operation(summary = "获取角色绑定的菜单")
    @Record(value = "获取角色绑定的菜单",label = LabelType.QUERY)
    public List<SystemMenuView> getRoleMenuList(@RequestParam("id")String id){
        return systemRoleService.getBindMenu(id);
    }


}
