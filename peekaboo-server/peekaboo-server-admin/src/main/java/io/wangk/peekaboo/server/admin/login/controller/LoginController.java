package io.wangk.peekaboo.server.admin.login.controller;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtils;
import cn.ruixi.azure.rainbow.boot.common.util.RequestUtil;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRoleMenuDTO;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRolePermissionDTO;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemUserDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemUserRoleDTO;
import cn.ruixi.azure.rainbow.boot.module.system.enums.LabelType;
import cn.ruixi.azure.rainbow.boot.module.system.login.entity.LoginView;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemUsers;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemLogMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemLogService;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemRoleService;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemUserService;
import cn.ruixi.azure.rainbow.boot.security.config.SecurityProperties;
import cn.ruixi.azure.rainbow.boot.security.core.annotations.RefreshToken;
import cn.ruixi.azure.rainbow.boot.security.core.util.JwtTokenManager;
import cn.ruixi.azure.rainbow.boot.security.core.util.SecurityFrameworkUtils;
import cn.ruixi.azure.rainbow.component.record.annotation.Record;
import cn.ruixi.azure.rainbow.component.web.core.response.Response;
import cn.ruixi.azure.rainbow.component.web.core.response.Responses;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author bijie
 * @since 2024/3/22
 */
@RestController
@Slf4j
public class LoginController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SystemUserService systemUserApi;

    @Autowired
    private SystemRoleService systemRoleApi;



    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private RequestUtil requestUtil;


    /**
     * 用户登录接口。
     * 使用JSON Web Token（JWT）进行认证和授权。
     *
     * @param loginView 包含登录所需信息的视图模型，如用户名和密码。
     * @param response HTTP响应，用于返回认证token。
     * @return 如果登录成功，返回包含token的成功响应；如果登录失败，返回错误信息。
     */
    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "登录接口")
    @Record(value = "登录接口",label = LabelType.LOGIN)
    public Response login(@RequestBody LoginView loginView, HttpServletResponse response) {
        // 首先，根据用户名查询用户，判断用户是否存在且未被删除
        SystemUserDto systemUserDetailsByUserName = systemUserApi.getSystemUserDetailsByUserName(loginView.getUsername());

        if (Objects.isNull(systemUserDetailsByUserName)) {
            return Responses.ofError("用户不存在，禁止登录");
        }

        if (systemUserDetailsByUserName.getStatus() == 1){
            return Responses.ofError("用户已被禁用，禁止登录");
        }
        // 创建认证令牌
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(loginView.getUsername(), loginView.getPassword());
        try {
            // 进行用户认证
            Authentication authenticate = authenticationManager.authenticate(authentication);
            // 将认证结果绑定到安全上下文中
            SecurityFrameworkUtils.setAuthentication(authenticate);
            // 生成JWT Token
            String token = jwtTokenManager.createToken(authenticate);
            // 将Token写入HTTP响应头
            response.addHeader(securityProperties.getTokenHeader(), token);
            // 返回成功响应，包含token
            return Responses.ofSuccess(token);
        } catch (BadCredentialsException | AccountExpiredException e) {
            // 记录登录失败信息
            log.error("登陆失败", authentication);
            // 返回用户名或密码错误的响应
            return Responses.ofError("用户名或密码错误");
        } catch (DisabledException disabledException) {
            // 返回用户被禁用的错误响应
            return Responses.ofError("用户状态异常");
        }
    }

    /**
     * 获取当前登录用户的信息。
     *
     * @param principal 当前请求的用户主体信息，用于识别登录用户。
     * @return 返回用户信息的响应对象。如果用户未登录，返回错误信息；否则，返回用户详细信息包括角色和菜单权限。
     */
    @GetMapping("/info")
    @Operation(summary = "当前登录用户信息")
    @Record(value = "当前登录用户信息" ,label = LabelType.QUERY)
    @RefreshToken
    public Response getUserInfo(Principal principal) {

        if (Objects.isNull(principal)) {
            // 检查用户是否已登录，未登录则返回错误信息
            return Responses.ofError("用户未登陆");
        }

        String name = principal.getName();
        //获取当前登录名称

        Map<String, Object> map = new HashMap<>();
        // 查询并填充用户的个人信息
        SystemUserDto systemUserDto = systemUserApi.getSystemUserDetailsByUserName(name);
        map.put("username", systemUserDto.getUsername());
        map.put("realname", systemUserDto.getNickname());
        map.put("avatar",   systemUserDto.getAvatar());
        if (Objects.nonNull(systemUserDto.getAvatar())) {
            map.put("avatar", requestUtil.getUrl() + systemUserDto.getAvatar());
        }
        map.put("deptname", systemUserDto.getDeptName());
        map.put("userId",systemUserDto.getId());
        map.put("sex",systemUserDto.getSex());
        map.put("deptId",systemUserDto.getDeptId());
        map.put("email",systemUserDto.getEmail());
        map.put("phone",systemUserDto.getMobile());
        map.put("remark",systemUserDto.getRemark());

        // 获取并填充用户的角色信息
        Set<SystemUserRoleDTO> userRoleList = systemUserApi.selectUserRoleList(systemUserDto.getId());
        map.put("roles", userRoleList);

        // 获取去重后的角色菜单树形结构
        List<String> roleIdList = userRoleList.stream()
                .map(SystemUserRoleDTO::getRoleId) // 提取角色ID
                .collect(Collectors.toList()); // 收集所有角色ID
        List<SystemRoleMenuDTO> roleMenuList = systemRoleApi.getRoleMenuList(roleIdList);
        map.put("menuList", roleMenuList);

        // 获取资源获取权限key
        List<SystemRolePermissionDTO> roleMenuListAuthKey = systemRoleApi.getRoleMenuListAuthKey(roleIdList);
        List<String> collect = roleMenuListAuthKey.stream().map(data -> data.getPermission()).collect(Collectors.toList());
        map.put("auth_key", collect);

        return Responses.ofSuccess(map);
    }





    /**
     * 处理用户退出登录的请求。
     * <p>
     * 该接口不需要接收任何参数，调用后会清除当前用户的认证信息，实现用户登出功能。
     * </p>
     *
     * @return 返回一个包含成功信息的响应对象
     */
    @GetMapping("/returnLogin")
    @Operation(summary = "退出登录")

    public Response returnLogin(HttpServletRequest request,HttpServletResponse response) {
        SystemLog systemLog = new SystemLog();
        Date date = new Date();
        long startTime = date.getTime();
        systemLog.setStartTime(DateUtils.getCurrentTime());

        // 清除当前用户的认证信息
        // 获取当前登录用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        LambdaQueryWrapper<SystemUsers> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUsers::getUsername, name);

        SystemUsers one = systemUserApi.getOne(wrapper);

        //记录日志

        systemLog.setId(IdUtils.create());
        systemLog.setLogContent("退出登录");
        systemLog.setLogType(LabelType.LOGINOUT);
        systemLog.setCreateUser(name);
        systemLog.setNickName(one.getNickname());
        systemLog.setRequestParam(request.getQueryString());

        systemLog.setUsername(name);
        systemLog.setIpAddr(RequestUtil.getRemoteIp(request));
        systemLog.setCreateTime(DateUtils.getCurrentTime());
        systemLog.setRequestUrl(request.getRequestURI());
        systemLog.setRequestType(request.getMethod());
        if (authentication != null) {
            // 注销处理器，Spring Security自带的处理器会清除SecurityContext和HttpSession
            LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, authentication);

            // 或者手动清理
            SecurityContextHolder.clearContext();

            // 清除HttpSession
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }


        }
        Date stopTime = new Date();
        systemLog.setCostTime(new BigDecimal(stopTime.getTime() - startTime));
        systemLog.setStopTime(DateUtils.getCurrentTime());
        Response success = Responses.SUCCESS;
        String result = JSON.toJSONString(success);
        systemLog.setResult(result);
        systemLogService.save(systemLog);
        return Responses.SUCCESS;
    }
}
