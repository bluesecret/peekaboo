package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDictTypeDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDictTypeVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDictTypeService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import com.google.common.base.Preconditions;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: bijie
 * @Date: 2024-01-11 15:31
 */
@RestController
@Slf4j
@RequestMapping("/type")
@RefreshToken
public class SystemDictTypeController {

    @Resource
    private SystemDictTypeService systemDictTypeService;

    /**
     * 获取字典类型列表
     * @param systemDictTypeDto
     * @return
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:query')")
    @PostMapping("/getSystemDictTypeList")
    @Operation(summary = "获取字典类型列表")
    @Record(value = "获取字典类型列表",label = LabelType.QUERY)
    public Pages getSystemDictTypeList(@RequestBody SystemDictTypeDto systemDictTypeDto){
       Pages pages= systemDictTypeService.getSystemDictTypeList(systemDictTypeDto);
        return pages;
    }

    /**
     * 删除字典类型
     * @param id
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:delete')")
    @GetMapping("/deleteSystemDictType")
    @Operation(summary = "删除字典类型")
    @Record(value = "删除字典类型",label = LabelType.DELETE)
    public void deleteSystemDictType(@RequestParam("id") String id){
        Preconditions.checkNotNull(id,"字典id不能为空");
        systemDictTypeService.deleteSystemDictType(id);
    }

    /**
     * 添加字典类型
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:delete')")
    @PostMapping("/addSystemDictType")
    @Operation(summary = "添加字典类型")
    @Record(value = "添加字典类型",label = LabelType.CREATE)
    public void addSystemDictType(@RequestBody SystemDictTypeDto systemDictTypeDto){
        systemDictTypeService.addSystemDictType(systemDictTypeDto);
    }

    /**
     * 更新字典类型
     */
    //@PreAuthorize("@ps.hasPermission('system:dict:update')")
    @PostMapping("/updateSystemDictType")
    @Operation(summary = "更新字典类型")
    @Record(value = "更新字典类型",label = LabelType.UPDATE)
    public void updateSystemDictType(@RequestBody SystemDictTypeDto systemDictTypeDto){
        systemDictTypeService.updateSystemDictType(systemDictTypeDto);
    }

    /**
     * 获取字典类型详情
     */
    //@PreAuthorize("@ps.hasPermission('system:dict:query')")
    @GetMapping("/getSystemDictTypeDetails")
    @Operation(summary = "获取字典类型详情")
    @Record(value = "获取字典类型详情",label = LabelType.QUERY)
    public SystemDictTypeVo getSystemDictTypeDetails(@RequestParam("id") String id){
        SystemDictTypeVo systemDictTypeVo= systemDictTypeService.getSystemDictTypeDetails(id);
        return systemDictTypeVo;
    }

    /**
     * 获取字典类型列表(不分页)
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:query')")
    @GetMapping("/getDictTypeList")
    @Operation(summary = "获取字典类型列表(不分页)")
    @Record(value = "获取字典类型列表(不分页)",label = LabelType.QUERY)
    public List<SystemDictTypeVo> getDictTypeList(){
        return systemDictTypeService.getDictTypeList();
    }

    /**
     * 获取所有type字典的数据（包括data）
     */
    @GetMapping("/getAllTypeDataList")
    @Operation(summary = "获取所有type字典的数据（包括data）")
    @Record(value = "获取所有type字典的数据（包括data）",label = LabelType.QUERY)
    public Map<String,Object> getAllTypeDataList(){
        return systemDictTypeService.getAllTypeDataList();
    }
}
