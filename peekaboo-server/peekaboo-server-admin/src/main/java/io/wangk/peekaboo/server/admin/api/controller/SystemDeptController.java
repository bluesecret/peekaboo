package io.wangk.peekaboo.server.admin.api.controller;


import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDeptDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptTreeDetailVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptTreeVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDepartmentService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import cn.ruixi.azure.rainbow.component.web.core.response.Response;
import cn.ruixi.azure.rainbow.component.web.core.response.Responses;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-04 11:03
 */
@RestController
@Slf4j
@RequestMapping("/dept")
@RefreshToken
public class SystemDeptController {

    @Resource
    private SystemDepartmentService systemDepartmentApi;

    /**
     * 添加部门
     */
  //  @PreAuthorize("@ps.hasPermission('system:dept:create')")
    @PostMapping("/addDepartment")
    @Operation(summary = "新增部门")
    @Record(value = "新增部门",label = LabelType.QUERY)
    public void addDepartment(@RequestBody SystemDeptDto sysDepartmentDto){
        systemDepartmentApi.addDepartment(sysDepartmentDto);
    }


    @GetMapping("/findById")
    @Operation(summary = "部门详情")
    @Record(value = "部门详情",label = LabelType.QUERY)
    public Response findById(@RequestParam String id){
        return Responses.ofSuccess(systemDepartmentApi.findById(id));
    }


    /**
     * 删除部门
     */
   // @PreAuthorize("@ps.hasPermission('system:dept:delete')")
    @GetMapping("/deleteDepartment")
    @Operation(summary = "删除部门")
    @Record(value = "删除部门",label = LabelType.DELETE)
    public void deleteDepartment(@RequestParam("id")String id){
        systemDepartmentApi.deleteDepartment(id);
    }

    /**
     * 编辑部门
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:update')")
    @PostMapping("/uploadSysDepartment")
    @Operation(summary = "修改部门")
    @Record(value = "修改部门",label =LabelType.UPDATE)
    public void uploadSysDepartment(@RequestBody SystemDeptDto sysDepartmentDto){
        systemDepartmentApi.uploadSysDepartment(sysDepartmentDto);
    }

    /**
     * 查询部门列表(树形)
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:query')")
    @GetMapping("/getSysDepartmentListStatus")
    @Operation(summary = "用户管理列表获取部门列表（树形结构排除状态）")
    @Record(value = "获取部门列表（树形结构）",label =LabelType.QUERY)
    public List<SystemDeptTreeVo> getSysDepartmentListStatus(){
            List<SystemDeptTreeVo> sysDepartmentVoList =systemDepartmentApi.getSysDepartmentListStatus();
            return sysDepartmentVoList;
    }
    /**
     * 查询部门列表(树形)
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:query')")
    @GetMapping("/getSysDepartmentList")
    @Operation(summary = "获取部门列表（树形结构）")
    @Record(value = "获取部门列表（树形结构）",label =LabelType.QUERY)
    public List<SystemDeptTreeVo> getSysDepartmentTreeList(){
        List<SystemDeptTreeVo> sysDepartmentVoList =systemDepartmentApi.getSysDepartmentTreeList();
        return sysDepartmentVoList;
    }

    /**
     * 查询部门列表(树形)
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:query')")
    @GetMapping("/getSysDepartmentDetailList")
    @Operation(summary = "获取部门列表详情（树形结构）")
    @Record(value = "获取部门列表详情（树形结构）",label =LabelType.QUERY)
    public List<SystemDeptTreeDetailVo> getSysDepartmentDetailList(@RequestParam(value = "name",required = false)String name,
                                                                   @RequestParam(value = "status",required = false)String status){
        return systemDepartmentApi.getSysDepartmentDetailList(name,status);
    }

    /**
     * 查询部门列表(树形)
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:query')")
    @GetMapping("/getPageSysDeptTreeDetailList")
    @Operation(summary = "获取部门列表详情（树形结构）带分页 ")
    @Record(value = "获取部门列表详情（树形结构）带分页 ",label =LabelType.QUERY)
    public IPage getPageSysDeptTreeDetailList(@RequestParam(value = "name",required = false)String name,
                                              @RequestParam(value = "status",required = false)String status,
                                              @RequestParam(value = "currentPage",required = false,defaultValue = "1")Integer currentPage,
                                              @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return systemDepartmentApi.getPageSysDeptTreeDetailList(name,status,currentPage,pageSize);
    }
    /**
     *获取所有部门 (列表)
     */
    //@PreAuthorize("@ps.hasPermission('system:dept:query')")
    @GetMapping("/getParentSysDepartmentList")
    @Operation(summary = "获取部门列表（不分页列表）")
    @Record(value = "获取部门列表（不分页列表）",label =LabelType.QUERY)
    public List<SystemDeptVo> getSysDepartmentList(){
            return systemDepartmentApi.getSysDepartmentList();
    }

    /**
     *修改部门负责人
     */
   // @PreAuthorize("@ps.hasPermission('system:dept:update')")
    @PostMapping("/updateDeptLeaders")
    @Operation(summary = "修改部门负责人")
    @Record(value = "修改部门负责人",label =LabelType.UPDATE)
    public void updateDeptLeaders(@RequestBody SystemDeptDto systemDeptDto){
        systemDepartmentApi.updateDeptLeaders(systemDeptDto);
    }
}
