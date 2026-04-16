package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDictDataDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDictDataVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDictData;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDictDataService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-11 15:30
 */
@RestController
@Slf4j
@RequestMapping("/dict")
@RefreshToken
public class SystemDictDataController {

    @Resource
    private SystemDictDataService systemDictDataService;

    /**
     * 获取字典数据列表
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:query')")
    @PostMapping("/getSystemDictDataList")
    @Operation(summary = "获取字典数据列表")
    @Record(value = "获取字典数据列表",label = LabelType.QUERY)
    public Pages getSystemDictDataList(@RequestBody SystemDictDataDto systemDictDataDto){
        Pages pages = systemDictDataService.getSystemDictDataList(systemDictDataDto);
        return pages;
    }

    /**
     *新增字典数据
     */
    //@PreAuthorize("@ps.hasPermission('system:dict:create')")
    @PostMapping("/addSystemDictData")
    @Operation(summary = "新增字典数据")
    @Record(value = "新增字典数据",label = LabelType.CREATE)
    public void  addSystemDictData(@RequestBody SystemDictDataDto systemDictDataDto){
        systemDictDataService.addSystemDictData(systemDictDataDto);
    }

    /**
     * 更新字典数据
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:update')")
    @PostMapping("/updateSystemDictData")
    @Operation(summary = "更新字典数据")
    @Record(value = "更新字典数据",label = LabelType.UPDATE)
    public void updateSystemDictData(@RequestBody SystemDictDataDto systemDictDataDto){
        systemDictDataService.updateSystemDictData(systemDictDataDto);
    }

    /**
     *删除字典数据
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:delete')")
    @GetMapping("/deleteSystemDictData")
    @Operation(summary = "删除字典数据")
    @Record(value = "删除字典数据",label = "Delete")
    public void deleteSystemDictData(@RequestParam("id") String id){
        systemDictDataService.deleteSystemDictData(id);
    }

    /**
     * 获取字段数据详情
     */
   // @PreAuthorize("@ps.hasPermission('system:dict:query')")
    @GetMapping("/getSystemDictDataDetails")
    @Operation(summary = "获取字段数据详情")
    @Record(value = "获取字段数据详情",label = "Query")
    public SystemDictDataVo getSystemDictDataDetails(@RequestParam("id") String id){
        SystemDictDataVo systemDictDataVo= systemDictDataService.getSystemDictDataDetails(id);
        return systemDictDataVo;
    }


    @GetMapping("/getDictDataByDictType")
    @Operation(summary = "根据字典类型查询字典数据（不分页）")
    @Record(value = "根据字典类型查询字典数据（不分页）",label = "Query")
    public List<SystemDictData> getDictDataByDictType(@RequestParam("dictType") String dictType){
        return systemDictDataService.getDictDataByDictType(dictType);
    }



    @GetMapping("/getAllDictType")
    @Operation(summary = "获取所有字典的数据")
    @Record(value = "获取所有字典的数据",label = "Query")
    public HashMap<String, Object> getAllTypeDataList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", systemDictDataService.getAllTypeDataList());
        return map;
    }

}
