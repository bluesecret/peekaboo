package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemPostDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemPostVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemPostService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import com.google.common.base.Preconditions;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: bijie
 * @Date: 2024-01-10 08:59
 */
@RestController
@Slf4j
@RefreshToken
@RequestMapping("/post")
public class SystemPostController {

    @Resource
    private SystemPostService systemPostService;

    /**
     * 获取岗位列表
     * @return
     */
   // @PreAuthorize("@ps.hasPermission('system:post:query')")
    @PostMapping("/getSystemPost")
    @Operation(summary = "获取岗位列表分页")
    @Record(value = "获取岗位列表分页",label = LabelType.QUERY)
    public Pages getSystemPost(@RequestBody SystemPostDto systemPostDto){
        Pages pages= systemPostService.getSystemPost(systemPostDto);
        return  pages;
    }

    /**
     * 删除岗位
     */
   // @PreAuthorize("@ps.hasPermission('system:post:delete')")
    @GetMapping("/deleteSystemPost")
    @Operation(summary = "删除岗位")
    @Record(value = "删除岗位",label = LabelType.DELETE)
    public void deleteSystemPost(@RequestParam("id")String id){
        Preconditions.checkNotNull(id, "id不能为空");
        systemPostService.deleteSystemPost(id);
    }

    /**
     * 修改岗位状态
     */
    //@PreAuthorize("@ps.hasPermission('system:post:update')")
    @PostMapping("/updateSystemPostStatus")
    @Operation(summary = "修改岗位状态")
    @Record(value = "修改岗位状态",label = LabelType.UPDATE)
    public void updateSystemPostStatus(@RequestBody SystemPostDto systemPostDto){
        systemPostService.updateSystemPostStatus(systemPostDto);
    }

    /**
     * 添加岗位
     */
   // @PreAuthorize("@ps.hasPermission('system:post:create')")
    @PostMapping("/addSystemPost")
    @Operation(summary = "添加岗位")
    @Record(value = "添加岗位",label = LabelType.CREATE)
    public void addSystemPost(@RequestBody SystemPostDto systemPostDto){
        systemPostService.addSystemPost(systemPostDto);
    }

    /**
     * 修改岗位信息
     */
    //@PreAuthorize("@ps.hasPermission('system:post:update')")
    @PostMapping("/updateSystemPost")
    @Operation(summary = "修改岗位信息")
    @Record(value = "修改岗位信息",label = LabelType.UPDATE)
    public void updateSystemPost(@RequestBody SystemPostDto systemPostDto){
        systemPostService.updateSystemPost(systemPostDto);
    }

    /**
     * 获取岗位详情
     */
   // @PreAuthorize("@ps.hasPermission('system:post:query')")
    @GetMapping("/getSystemPostDetails")
    @Operation(summary = "获取岗位详情")
    @Record(value = "获取岗位详情",label = LabelType.QUERY)
    public SystemPostVo getSystemPostDetails(@RequestParam("id") String id){
        SystemPostVo systemPostVo= systemPostService.getSystemPostDetails(id);
        return systemPostVo;
    }

    /**
     * 获取岗位列表
     */
    @GetMapping("/getPostList")
    @Operation(summary = "获取岗位列表不分页")
    @Record(value = "获取岗位列表不分页",label = LabelType.QUERY)
    public List<SystemPostVo> getPostList(){
        List<SystemPostVo> list =systemPostService.getPostList();
        return list;
    }




}
