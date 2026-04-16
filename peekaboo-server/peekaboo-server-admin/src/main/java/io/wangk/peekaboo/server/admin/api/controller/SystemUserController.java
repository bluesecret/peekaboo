package io.wangk.peekaboo.server.admin.api.controller;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.PasswordEncode;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemUserDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemUserImportVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemUserVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemUsersView;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemUserService;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import cn.ruixi.azure.rainbow.component.web.core.response.Response;
import cn.ruixi.azure.rainbow.component.web.core.response.Responses;
import com.alibaba.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: bijie
 * @Date: 2024-01-08 10:31
 */
@RestController
@Slf4j
@RequestMapping("/user")
@RefreshToken
public class SystemUserController {

    @Resource
    private SystemUserService systemUserService;

    /**
     * 获取用户列表
     * @param sysUserDto
     * @return
     */
//    @PostMapping("/getPageSysUserList")
//    @Operation(summary = "用户列表查询（带分页）")
//    @Record(value = "用户列表查询（带分页）",label = LabelType.QUERY)
//    public Pages getPageSysUserList(@RequestBody SystemUserDto sysUserDto){
//       return systemUserService.getPageSysUserList(sysUserDto);
//    }
    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("/getSysUserList")
    @Operation(summary = "用户列表查询(不分页)")
    @Record(value = "用户列表查询(不分页)",label = LabelType.QUERY)
    public Response getSysUserList(){
        return Responses.ofSuccess(systemUserService.getSysUserList());
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
  //  @PreAuthorize("@ps.hasPermission('system:user:delete')")
    @GetMapping("/deleteSystemUserById")
    @Operation(summary = "删除用户")
    @Record(value = "删除用户",label = LabelType.DELETE)
    public void deleteSystemUserById(@RequestParam("id") String id){
      systemUserService.deleteSystemUserById(id);
    }

    /**
     * 添加用户
     * @param systemUserDto
     * @return
     */
    // @PreAuthorize("@ps.hasPermission('system:user:create')")
     @PostMapping("/addSystemUser")
     @Operation(summary = "新增用户")
     @Record(value = "新增用户",label = LabelType.CREATE)
     public Response addSystemUser(@RequestParam(value = "file",required = false) MultipartFile file, SystemUserDto systemUserDto) {
        systemUserService.addSystemUser(file,systemUserDto);
        return Responses.SUCCESS;

    }

    /**
     * 更新用户信息
     * @param systemUserDto
     * @return
     */
  //  @PreAuthorize("@ps.hasPermission('system:user:update')")
    @PostMapping("/updateSystemUser")
    @Operation(summary = "修改用户")
    @Record(value = "修改用户",label = LabelType.UPDATE)
    public void updateSystemUser(@RequestParam(value = "file",required = false) MultipartFile file,SystemUserDto systemUserDto){
       systemUserService.updateSystemUser(file,systemUserDto);
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
  //  @PreAuthorize("@ps.hasPermission('system:user:query')")
    @GetMapping("/getSystemUserDetails")
    @Operation(summary = "获取用户详情信息")
    @Record(value = "获取用户详情信息",label = LabelType.QUERY)
    public SystemUserVo getSystemUserDetails(@RequestParam("id")String id){
        return systemUserService.getSystemUserDetails(id);
    }

    /**
     * 修改用户状态
     */
    //@PreAuthorize("@ps.hasPermission('system:user:update')")
    @PostMapping("/updateUserStatus")
    @Operation(summary = "修改用户状态")
    @Record(value = "修改用户状态",label = LabelType.UPDATE)
    public void updateUserStatus(@RequestBody SystemUserDto systemUserDto){
        systemUserService.updateUserStatus(systemUserDto);
    }


    /**
     * 根据条件查询该部门下的用户
     */

    @PostMapping("/getSystemUserList")
    @Operation(summary = "根据部门条件查询该部门下的用户")
    @Record(value = "根据部门条件查询该部门下的用户",label = LabelType.QUERY)
    public Pages getSystemUserList(@RequestBody SystemUserDto systemUserDto){

        return systemUserService.getSystemUserByDept(systemUserDto);
    }

    /**
     * 重置密码
     */
    @GetMapping("/resetPassword")
    @Operation(summary = "重置密码")
    @Record(value = "重置密码",label = LabelType.UPDATE)
    public Response resetPassword(@RequestParam("id") String id){
        systemUserService.restartPassword(id);
        return Responses.SUCCESS;
    }

    /**
     * 修改用户状态
     */
    @GetMapping("/updateSystemUserStatus")
    @Operation(summary = "修改用户状态")
    @Record(value = "修改用户状态",label = LabelType.UPDATE)
    public Response updateSystemUserStatus(SystemUserDto systemUserDto){
        return systemUserService.updateSystemUserStatus(systemUserDto)==1?Responses.SUCCESS:Responses.ofError("1","操作失败");
    }


    @PostMapping("import")
    @Operation(summary = "用户导入")
    @Record(value = "用户导入",label = LabelType.IMPORTED)
    public void importUser(@RequestBody MultipartFile file){
        systemUserService.importUser(file);
    }


    @PostMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        systemUserService.export(response);
    }


    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    @Operation(summary = "修改密码")
    @Record(value = "修改密码",label = LabelType.UPDATE)
    public Response updatePassword(@RequestBody PasswordEncode passwordEncode){
        return systemUserService.updatePassword(passwordEncode);
    }


    /**
     * 修改头像
     */
    @PostMapping("/updateAvatar")
    @Operation(summary = "修改头像")
    @Record(value = "修改头像",label = LabelType.UPDATE)
    public Response updateAvatar(@RequestParam("file") MultipartFile file){
        return Responses.ofSuccess(systemUserService.updateAvatar(file));
    }


    /**
     * 修改用户资料
     */
    @PostMapping("/updateUserInfo")
    @Operation(summary = "修改用户资料")
    @Record(value = "修改用户资料",label = LabelType.UPDATE)
    public Response updateUserInfo(@RequestBody SystemUsersView systemUsersView){
        return Responses.ofSuccess(systemUserService.updateUserInfo(systemUsersView));
    }
}