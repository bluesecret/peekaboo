package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemMenuDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuView;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemMenuService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import com.google.common.base.Preconditions;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jdk.jfr.Frequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-09 11:05
 */
@RestController
@Slf4j
@RefreshToken
@RequestMapping("/menu")
public class SystemMenuController {

    @Resource
    private SystemMenuService systemMenuService;


    /**
     * 获取菜单列表(树形)
     * @return
     */
    //@PreAuthorize("@ps.hasPermission('system:menu:query')")
    @GetMapping("/getSystemMenuTreeList")
    @Operation(summary = "获取菜单列表(树形)")
    @Record(value = "获取菜单列表(树形)",label = LabelType.QUERY)
    public List<SystemMenuVo> getSystemMenuList(@RequestParam(value = "name",required = false)String name,
                                                @RequestParam(value = "status",required = false)String status,
                                                @RequestParam(value = "visible",required = false)String visible){
        return systemMenuService.getSystemMenuList(name,status,visible);
    }


    @GetMapping("/getSystemMenuTreeStatusList")
    @Operation(summary = "获取菜单列表(树形)")
    @Record(value = "获取菜单列表(树形)",label = LabelType.QUERY)
    public List<SystemMenuVo> getSystemMenuTreeStatusList(){
        return systemMenuService.getSystemMenuTreeStatusList();
    }
    /**
     * 删除菜单
     */
    //@PreAuthorize("@ps.hasPermission('system:menu:delete')")
    @GetMapping("/deleteSystemMenu")
    @Operation(summary = "删除菜单")
    @Record(value = "删除菜单",label = LabelType.DELETE)
    public void deleteSystemMenu(@RequestParam("id")String id){
        Preconditions.checkNotNull(id,"菜单id不能为空");
        systemMenuService.deleteSystemMenu(id);
    }

    /**
     * 获取菜单详情
     */
   // @PreAuthorize("@ps.hasPermission('system:menu:query')")
    @GetMapping("/getSystemDetails")
    @Operation(summary = "获取菜单详情")
    @Record(value = "获取菜单详情",label = LabelType.QUERY)
    public SystemMenuView getSystemDetails(@RequestParam("id")String id){
        return  systemMenuService.getSystemDetails(id);
    }

    /**
     * 更新菜单
     */
    //@PreAuthorize("@ps.hasPermission('system:menu:update')")
    @PostMapping("/updateSystemMenu")
    @Operation(summary = "更新菜单")
    @Record(value = "更新菜单",label = LabelType.UPDATE)
    public void updateSystemMenu(@RequestBody SystemMenuDto systemMenuDto){
        systemMenuService.updateSystemMenu(systemMenuDto);
    }

    /**
     * 添加菜单
     * @param systemMenuDto
     * @return
     */
    //@PreAuthorize("@ps.hasPermission('system:menu:create')")
    @PostMapping("/addSystemMenu")
    @Operation(summary = "添加菜单")
    @Record(value = "添加菜单",label = LabelType.CREATE)
    public void addSystemMenu(@RequestBody SystemMenuDto systemMenuDto){
        systemMenuService.addSystemMenu(systemMenuDto);
    }

    /**
     * 获取菜单列表(列表)
     */
    //@PreAuthorize("@ps.hasPermission('system:menu:query')")
    @GetMapping("/getMenuList")
    @Operation(summary = "获取菜单列表(列表不分页)")
    @Record(value = "获取菜单列表(列表不分页)",label = LabelType.QUERY)
    public List<SystemMenuView> getMenuList(){
        return systemMenuService.getMenuList();
    }


    /**
     * 获取菜单目录列表(树形)type=1
     */
    @GetMapping("/getMenuListTree")
    @Operation(summary = "获取菜单目录列表(树形)type=1")
    @Record(value = "获取菜单目录列表(树形)type=1",label = LabelType.QUERY)
    public List<SystemMenuVo> getMenuListTree(){
        return systemMenuService.getMenuListTree();
    }
}
