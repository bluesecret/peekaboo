package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import cn.idev.excel.read.listener.PageReadListener;
import cn.idev.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import io.wangk.peekaboo.common.enums.CommonStatusEnum;
import io.wangk.peekaboo.common.exception.StatusException;
import io.wangk.peekaboo.common.utils.FileUtil;
import io.wangk.peekaboo.common.utils.IdUtils;
import io.wangk.peekaboo.common.utils.PeekabooContext;
import io.wangk.peekaboo.common.utils.object.BeanUtils;
import io.wangk.peekaboo.component.web.core.response.Responses;
import io.wangk.peekaboo.server.admin.api.dto.bo.PasswordEncode;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserDto;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserRoleDTO;
import io.wangk.peekaboo.server.admin.api.dto.vo.*;
import io.wangk.peekaboo.server.admin.enums.IsDeleteEnum;
import io.wangk.peekaboo.server.admin.repository.entity.*;
import io.wangk.peekaboo.server.admin.repository.mapper.*;
import io.wangk.peekaboo.server.admin.repository.service.SystemUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: bijie
 * @Date: 2024-01-08 10:59
 */

@Slf4j
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUsersMapper, SystemUsers> implements SystemUserService {

    @Resource
    private SystemUsersMapper systemUsersMapper;

    @Resource
    private SystemUserRoleMapper systemUserRoleMapper;

    @Resource
    private SystemUserPostMapper systemUserPostMapper;

    @Resource
    private SystemDeptMapper systemDeptMapper;

    @Autowired
    private SystemRoleMapper systemRoleMapper;

    @Autowired
    private SystemUserDeptMapper systemUserDeptMapper;

    @Autowired
    private PeekabooContext peekabooContext;

    @Autowired
    private SystemPostMapper systemPostMapper;
    @Autowired
    private Environment env;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystemUserById(String id) {
        //校验用户是否存在
        String[] strings = id.split(",");
        for (String string : strings) {
            //系统默认admin角色不可删除
            SystemUsers systemUsers = systemUsersMapper.selectById(string);
            if (systemUsers.getUsername().equals("admin")) {
                throw new StatusException("系统默认admin角色不可删除");
            }
            validateUserExists(string);
            SystemUsers users = SystemUsers.builder().id(string).deleted(IsDeleteEnum.DISABLE.getType()).build();
            systemUsersMapper.updateById(users);
            //删除用户与角色关系
            List<String> userRoleIds = getUserRoleIds(string);
            if (!CollectionUtils.isEmpty(userRoleIds)) {
                systemUserRoleMapper.deleteBatchIds(userRoleIds);
            }
            //删除岗位关系
            List<String> userPostIds = getUserPostIds(string);
            if (!CollectionUtils.isEmpty(userPostIds)) {
                systemUserPostMapper.deleteBatchIds(userPostIds);
            }
            //删除部门关系
            List<String> userDeptIds = getUserDeptIds(string);
            if (!CollectionUtils.isEmpty(userDeptIds)) {
                systemUserDeptMapper.deleteBatchIds(userDeptIds);
            }
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSystemUser(MultipartFile file, SystemUserDto systemUserDto) {
        SystemUsers systemUsers = BeanUtils.toBean(systemUserDto, SystemUsers.class);
        //判断是否有重复数据
        checkDuplicateData(systemUserDto);
        //添加用户
        systemUsers.setDeleted(IsDeleteEnum.NORMAL.getType());
        //保存头像到本地/Oss
        if (file != null) {
            String savePath = FileUtil.saveFile(file, env.getProperty("system.user.file.avatar"));
            String mvcpath = savePath.substring(savePath.lastIndexOf(File.separator) + 1);
            systemUsers.setAvatar(env.getProperty("system.user.file.prefix") + mvcpath);
        }
        systemUsers.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String id = IdUtils.create();
        systemUsers.setId(id);
        //设置密码
        systemUsers.setPassword(BCrypt.hashpw(env.getProperty("system.user.password")));
        int insert = systemUsersMapper.insert(systemUsers);
        if (insert > 0) {
            //角色列表
            List<String> roleIds = systemUserDto.getRoleIds();
            //添加用户角色关系
            List<SystemUserRole> userRoles = new ArrayList<>();
            if (!CollectionUtils.isEmpty(systemUserDto.getRoleIds())) {
                for (String roleId : roleIds) {
                    SystemUserRole userRole = new SystemUserRole();
                    userRole.setId(IdUtils.create());
                    userRole.setUserId(id);
                    userRole.setRoleId(roleId);
                    userRole.setDeleted(IsDeleteEnum.NORMAL.getType());
                    userRole.setStatus(IsDeleteEnum.NORMAL.getType().toString());
                    userRoles.add(userRole);
                }
            }
            //批量添加关系
            Db.saveBatch(userRoles);
            List<SystemUserPost> systemUserPostList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(systemUserDto.getPostIds()) && !"undefined".equals(systemUserDto.getPostIds().get(0))) {
                //添加岗位关系
                for (String postId : systemUserDto.getPostIds()) {
                    SystemUserPost userPost = new SystemUserPost();
                    userPost.setId(IdUtils.create());
                    userPost.setUserId(id);
                    userPost.setPostId(postId);
                    userPost.setDeleted(IsDeleteEnum.NORMAL.getType());
                    userPost.setStatus(IsDeleteEnum.NORMAL.getType().toString());
                    systemUserPostList.add(userPost);
                }
                //批量添加关系
                Db.saveBatch(systemUserPostList);
            }

            //部门
            List<String> deptIds = systemUserDto.getDeptIds();
            List<SystemUserDept> userDeptIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(systemUserDto.getDeptIds())) {
                for (String deptId : deptIds) {
                    SystemUserDept userDept = new SystemUserDept();
                    userDept.setId(IdUtils.create());
                    userDept.setUserId(id);
                    userDept.setDeptId(deptId);
                    userDept.setStatus(IsDeleteEnum.NORMAL.getType().toString());
                    userDeptIds.add(userDept);
                }
            }
            //批量添加关系
            Db.saveBatch(userDeptIds);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemUser(MultipartFile file, SystemUserDto systemUserDto) throws IOException {
        //校验用户是否存在
        validateUserExists(systemUserDto.getId());

        //检验是否重复数据
        SystemUsers systemUsers = systemUsersMapper.selectById(systemUserDto.getId());
        if (!systemUserDto.getUsername().equals(systemUsers.getUsername())) {
            //校验是否重复
            checkDuplicateData(systemUserDto);
        }
        SystemUsers users = BeanUtils.toBean(systemUserDto, SystemUsers.class);
        //更新角色关系先删除在添加

        //先查询原来关系
        List<String> ids = getUserRoleIds(systemUserDto.getId());
        //删除关系
        if (!CollectionUtils.isEmpty(ids)) {
            LambdaQueryWrapper<SystemUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemUserRole::getUserId, systemUserDto.getId());
            systemUserRoleMapper.delete(wrapper);
        }
        //添加关系
        List<SystemUserRole> userRoleList = new ArrayList<>();
        for (String roleId : systemUserDto.getRoleIds()) {
            SystemUserRole role = new SystemUserRole();
            role.setId(IdUtils.create());
            role.setUserId(systemUserDto.getId());
            role.setRoleId(roleId);
            role.setDeleted(IsDeleteEnum.NORMAL.getType());
            role.setStatus(IsDeleteEnum.NORMAL.getType().toString());
            userRoleList.add(role);
        }
        Db.saveBatch(userRoleList);

        //更新岗位关系先删除后添加
        //查询岗位关系
        List<String> userPostIds = getUserPostIds(systemUserDto.getId());
        if (!CollectionUtils.isEmpty(userPostIds)) {
            //删除关系
            LambdaQueryWrapper<SystemUserPost> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemUserPost::getUserId, systemUserDto.getId());
            systemUserPostMapper.delete(wrapper);
        }
        //添加关系
        List<SystemUserPost> postList = new ArrayList<>();
        for (String postId : systemUserDto.getPostIds()) {
            SystemUserPost userPost = new SystemUserPost();
            userPost.setId(IdUtils.create());
            userPost.setUserId(systemUserDto.getId());
            userPost.setPostId(postId);
            userPost.setDeleted(IsDeleteEnum.NORMAL.getType());
            userPost.setStatus(IsDeleteEnum.NORMAL.getType().toString());

            postList.add(userPost);
        }
        Db.saveBatch(postList);

        //更新岗位关系先删除后添加
        //查询岗位关系
        List<String> userDepts = getUserDeptIds(systemUserDto.getId());
        if (!CollectionUtils.isEmpty(userDepts)) {
            //删除关系
            LambdaQueryWrapper<SystemUserDept> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemUserDept::getUserId, systemUserDto.getId());
            systemUserDeptMapper.delete(wrapper);
        }
        //部门
        List<String> deptIds = systemUserDto.getDeptIds();
        //添加用户部门关系
        List<SystemUserDept> userDeptIds = new ArrayList<>();
        if (!Objects.isNull(deptIds.size())) {
            for (String deptId : deptIds) {
                SystemUserDept userDept = new SystemUserDept();
                userDept.setId(IdUtils.create());
                userDept.setUserId(systemUserDto.getId());
                userDept.setDeptId(deptId);
                userDept.setStatus(IsDeleteEnum.NORMAL.getType().toString());
                userDeptIds.add(userDept);
            }
        }
        //批量添加关系
        Db.saveBatch(userDeptIds);
        if (file != null) {
            String savePath = FileUtil.saveFile(file, env.getProperty("system.user.file.avatar"));
            String mvcpath = savePath.substring(savePath.lastIndexOf(File.separator) + 1);
            users.setAvatar(env.getProperty("system.user.file.prefix") + mvcpath);
        }

        systemUsersMapper.updateById(users);
    }

    @Override
    public SystemUserVo getSystemUserDetails(String id) {
        //获取信息
        SystemUsers user = systemUsersMapper.selectById(id);
        // 获取角色列表
        List<SystemUserRoleDTO> systemUserRoleDTOS = systemUserRoleMapper.selectUserRolesById(id);
        //获取 部门信息
        List<SystemUserDeptVo> userDeptListByUserId = systemUserDeptMapper.getUserDeptListByUserId(id);
        //获取 岗位信息
        List<SystemUserPostVo> userPostListByUserId = systemUserPostMapper.getUserPostListByUserId(id);
        SystemUserVo systemUserVo = BeanUtils.toBean(user, SystemUserVo.class);
        if (Strings.isNotBlank(user.getAvatar())) {
            systemUserVo.setAvatar(peekabooContext.getUrl() + user.getAvatar());
        }
        systemUserVo.setRoleIds(systemUserRoleDTOS.stream().map(SystemUserRoleDTO::getRoleId).collect(Collectors.joining(",")));
        systemUserVo.setRoleNames(systemUserRoleDTOS.stream().map(SystemUserRoleDTO::getRoleName).collect(Collectors.joining(",")));
        systemUserVo.setDeptIds(userDeptListByUserId.stream().map(SystemUserDeptVo::getDeptId).collect(Collectors.joining(",")));
        systemUserVo.setDeptNames(userDeptListByUserId.stream().map(SystemUserDeptVo::getDeptName).collect(Collectors.joining(",")));
        String collect = userPostListByUserId.stream().map(SystemUserPostVo::getPostId).collect(Collectors.joining(","));
        if (Strings.isBlank(collect)) {
            systemUserVo.setPostIds(null);
        } else {
            systemUserVo.setPostIds(collect);

        }
        systemUserVo.setPostNames(userPostListByUserId.stream().map(SystemUserPostVo::getPostName).collect(Collectors.joining(",")));

        return systemUserVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(SystemUserDto systemUserDto) {
        SystemUsers users = BeanUtils.toBean(systemUserDto, SystemUsers.class);
        systemUsersMapper.updateById(users);
    }

    @Override
    public SystemUserDto getSystemUserDetailsByUserName(String username) {

        return systemUsersMapper.selectByUsername(username);
    }

    @Override
    public Set<SystemUserRoleDTO> selectUserRoleList(String id) {
        return systemUserRoleMapper.selectUserRoleList(id);
    }

    @Override
    public List<SystemUserVo> getSystemUserByDept(SystemUserDto systemUserDto) {
        //获取部门下的所有用户信息和部门信息
        String deptId = systemUserDto.getDeptId();
        //获取满足查询条件的ids
        List<String> userIdsList = getUserIdsList(systemUserDto);
        //获取满足条件的部门下的所有子部门的ids
        List<String> deptIdsList = getDeptIdsList(deptId);
        systemUserDto.setDeptIds(deptIdsList);
        systemUserDto.setUserIds(userIdsList);
        //查询已绑定机构的用户列表  无法通过 查询控制禁用部门的信息 通过代码形式 操作
        List<SystemUserVo> userListByDept = systemUsersMapper.getUserListByDept(systemUserDto);
        //手动设置前端需要返回数据格式
        tranformUserRole(userListByDept);
        return userListByDept;
    }


    public List<String> getUserIdsList(SystemUserDto systemUserDto) {
        //查询满足搜索条件的用户
        LambdaQueryWrapper<SystemUsers> usersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        usersLambdaQueryWrapper.eq(SystemUsers::getDeleted, IsDeleteEnum.NORMAL.getType())
            .eq(Objects.nonNull(systemUserDto.getStatus()), SystemUsers::getStatus, CommonStatusEnum.ENABLE.getStatus())
            .like(Strings.isNotBlank(systemUserDto.getUsername()), SystemUsers::getUsername, systemUserDto.getUsername());
        List<SystemUsers> systemUsers = systemUsersMapper.selectList(usersLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(systemUsers)) {
            return systemUsers.stream().map(SystemUsers::getId).collect(Collectors.toList());
        }
        return null;
    }

    //获取部门下的所有子部门的ids
    public List<String> getDeptIdsList(String deptId) {
        //获取全部部门
        List<SystemDept> systemDeptList = systemDeptMapper.selectList(new LambdaQueryWrapper<SystemDept>()
            .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType()));
        if (Objects.equals("0", deptId)) {
            return systemDeptList.stream().map(SystemDept::getId).collect(Collectors.toList());
        }
        SystemDept systemDept = new SystemDept();
        systemDept.setId(deptId);
        //获取当前部门下面的子部门
        List<SystemDept> deptList = getDeptList(systemDept, systemDeptList);
        List<String> deptIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deptList)) {
            deptIds = deptList.stream().map(SystemDept::getId).collect(Collectors.toList());
        }
        //自身也需要添加进去
        deptIds.add(deptId);
        return deptIds;
    }

    public void tranformUserRole(List<SystemUserVo> systemUserVoList) {
        //设置部门的字符串数据返回给前端
        systemUserVoList.stream().forEach(record -> {
            record.setDeptNames(record.getDeptNameList().stream().collect(Collectors.joining(",")));
        });
        systemUserVoList.stream().forEach(record -> {
            record.setDeptIds(record.getDeptIdList().stream().collect(Collectors.joining(",")));
        });
        //查询已经禁用的机构
        List<String> disableDeptList = getDisableDeptList().stream().map(SystemDept::getId).collect(Collectors.toList());
        List<String> disableDeptNameList = getDisableDeptList().stream().map(SystemDept::getName).collect(Collectors.toList());
        //查询未绑定机构的用户列表
        systemUserVoList.forEach(record -> {
            //加上前缀
            if (Strings.isNotBlank(record.getAvatar())) {
                record.setAvatar(peekabooContext.getUrl() + record.getAvatar());
            }
            //判断如果用户的机构存在与禁用的
            ArrayList<String> newList = new ArrayList<>();
            ArrayList<String> deptNames = new ArrayList<>();
            if (Strings.isNotBlank(record.getDeptIds())) {
                List<String> deptStr = new ArrayList<>(Arrays.asList(record.getDeptIds().split(",")));
                for (String dept : deptStr) {
                    if (!disableDeptList.contains(dept)) {
                        newList.add(dept);
                    }
                }
            }
            if (Strings.isNotBlank(record.getDeptNames())) {
                List<String> deptName = new ArrayList<>(Arrays.asList(record.getDeptNames().split(",")));
                for (String name : deptName) {
                    if (!disableDeptNameList.contains(name)) {
                        deptNames.add(name);
                    }
                }
            }
            //重新设置部门信息
            record.setDeptIds(newList.stream().collect(Collectors.joining(",")));
            record.setDeptNames(deptNames.stream().collect(Collectors.joining(",")));

            //手动设置角色信息 避免关联表次数太多
            Set<SystemUserRoleDTO> systemUserRoles = systemUserRoleMapper.selectUserRoleList(record.getId());

            //获取当前用户角色
            record.setRoleIds(systemUserRoles.stream()
                .filter(data -> Objects.equals("0", data.getStatus()))
                .sorted(Comparator.comparing(SystemUserRoleDTO::getRoleId))
                .map(SystemUserRoleDTO::getRoleId)
                .collect(Collectors.joining(",")));
            record.setRoleNames(systemUserRoles.stream()
                .filter(data -> Objects.equals("0", data.getStatus()))
                .sorted(Comparator.comparing(SystemUserRoleDTO::getRoleId))
                .map(SystemUserRoleDTO::getRoleName)
                .collect(Collectors.joining(",")));
        });

    }

    /**
     * 获取已经禁用的机构
     */
    public List<SystemDept> getDisableDeptList() {
        return systemDeptMapper.selectList(new LambdaQueryWrapper<SystemDept>()
            .eq(SystemDept::getStatus, CommonStatusEnum.DISABLE.getStatus())
            .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType()));

    }

    /**
     * 获取当前部门下面的子部门
     */
    public List<SystemDept> getDeptList(SystemDept dept, List<SystemDept> systemDepts) {
        List<SystemDept> children = new ArrayList<>();
        for (SystemDept systemDept : systemDepts) {
            if (systemDept.getParentId().equals(dept.getId())) {
                children.add(systemDept);
                children.addAll(getDeptList(systemDept, systemDepts));
            }
        }
        return children;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restartPassword(String id) {
        SystemUsers systemUsers = systemUsersMapper.selectById(id);
        systemUsers.setPassword(BCrypt.hashpw(env.getProperty("system.user.password")));
        systemUsersMapper.updateById(systemUsers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSystemUserStatus(SystemUserDto systemUserDto) {
        return systemUsersMapper.updateById(BeanUtils.toBean(systemUserDto, SystemUsers.class));
    }

    @Override
    public List<SystemUserVo> getSysUserList() {
        LambdaQueryWrapper<SystemUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUsers::getDeleted, IsDeleteEnum.NORMAL.getType());
        List<SystemUsers> systemUsers = systemUsersMapper.selectList(queryWrapper);
        systemUsers.forEach(data -> data.setAvatar(peekabooContext.getUrl() + data.getAvatar()));
        return BeanUtils.toBean(systemUsers, SystemUserVo.class);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importUser(MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }
        List<SystemUserImportVo> list = new ArrayList<>();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        try {
            EasyExcelFactory.read(file.getInputStream(), SystemUserImportVo.class, new PageReadListener<SystemUserImportVo>(datalist -> {
                    list.addAll(datalist);
                }))
                .headRowNumber(1)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        insertUserInfo(list);
    }


    @Override
    public void export(HttpServletResponse response) throws IOException {
        ClassPathResource templateResource = new ClassPathResource("template/UserImportTemp.xlsx");
        if (templateResource.exists()) {
            String fileName = "用户导入模板";

            InputStream inputStream = templateResource.getInputStream();

            // 设置响应头，指示浏览器以附件形式下载文件
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");

            // 将模板文件内容写入响应流
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // 关闭输入流
            inputStream.close();
        } else {
            response.sendError(1, "Template file not found.");
        }
    }

    @Override
    public boolean updatePassword(PasswordEncode passwordEncode) {
        if (StringUtils.isEmpty(passwordEncode.getOldPassword()) || StringUtils.isEmpty(passwordEncode.getNewPassword()) || StringUtils.isEmpty(passwordEncode.getNewPassword_confirmation())) {
            throw new StatusException("密码不能为空");
        }
        if (!passwordEncode.getNewPassword().equals(passwordEncode.getNewPassword_confirmation())) {
            throw new StatusException("两次输入密码不一致");
        }
        SystemUsers systemUsers = getLoginUser();
        if (systemUsers.getStatus() == CommonStatusEnum.DISABLE.getStatus()) {
            throw new StatusException("用户被禁用");
        }
        //新旧密码不能一致
        if (passwordEncode.getOldPassword().equals(passwordEncode.getNewPassword())) {
            throw new StatusException("新旧密码不能一致");
        }
        if (BCrypt.checkpw(passwordEncode.getOldPassword(), systemUsers.getPassword())) {
            systemUsers.setPassword(BCrypt.hashpw(passwordEncode.getNewPassword()));
            systemUsersMapper.updateById(systemUsers);
            return true;
        } else {
            throw new StatusException("旧密码错误");
        }

    }

    @Override
    public boolean updateAvatar(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StatusException("请上传头像");
        }
        if (file.getSize() > 1024 * 1024 * 2) {
            throw new StatusException("头像大小不能超过2M");
        }
        SystemUsers systemUsers = getLoginUser();
        String savePath = FileUtil.saveFile(file, env.getProperty("system.user.file.avatar"));
        String mvcpath = savePath.substring(savePath.lastIndexOf(File.separator) + 1);
        systemUsers.setAvatar(env.getProperty("system.user.file.prefix") + mvcpath);
        systemUsersMapper.updateById(systemUsers);

        return true;
    }

    @Override
    public boolean updateUserInfo(SystemUsersView systemUsersView) {
        SystemUsers systemUsers = BeanUtils.toBean(systemUsersView, SystemUsers.class);
        SystemUsers loginUser = getLoginUser();
        systemUsers.setId(loginUser.getId());
        systemUsersMapper.updateById(systemUsers);
        return true;
    }


    private void insertUserInfo(List<SystemUserImportVo> users) {
        for (SystemUserImportVo user : users) {
            SystemUserDto systemUserDto = BeanUtils.toBean(user, SystemUserDto.class);

            if (!StringUtils.isEmpty(user.getDeptNames())) {
                List<String> deptIds = new ArrayList<>();
                LambdaQueryWrapper<SystemDept> wrapper = new LambdaQueryWrapper<>();
                List<String> deptName = new ArrayList<>(Arrays.asList(user.getDeptNames().split(",")));
                for (String name : deptName) {
                    wrapper.clear();
                    wrapper.eq(SystemDept::getName, name).eq(SystemDept::getStatus, CommonStatusEnum.ENABLE.getStatus())
                        .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType());
                    deptIds.addAll(systemDeptMapper.selectList(wrapper).stream().map(SystemDept::getId).collect(Collectors.toList()));
                }
                systemUserDto.setDeptIds(deptIds);
            }
            if (!StringUtils.isEmpty(user.getPostNames())) {
                List<String> postIds = new ArrayList<>();
                LambdaQueryWrapper<SystemPost> wrapper = new LambdaQueryWrapper<>();
                List<String> postNames = new ArrayList<>(Arrays.asList(user.getPostNames().split(",")));
                for (String postName : postNames) {
                    wrapper.clear();
                    wrapper.eq(SystemPost::getName, postName)
                        .eq(SystemPost::getStatus, CommonStatusEnum.ENABLE.getStatus()).eq(SystemPost::getDeleted, IsDeleteEnum.NORMAL.getType());
                    postIds.addAll(systemPostMapper.selectList(wrapper).stream().map(SystemPost::getId).collect(Collectors.toList()));
                }
                systemUserDto.setPostIds(postIds);
            }
            if (!StringUtils.isEmpty(user.getRoleNames())) {
                List<String> roleIds = new ArrayList<>();
                LambdaQueryWrapper<SystemRole> wrapper = new LambdaQueryWrapper<>();
                List<String> postNames = new ArrayList<>(Arrays.asList(user.getRoleNames().split(",")));
                for (String postName : postNames) {
                    wrapper.clear();
                    wrapper.eq(SystemRole::getName, postName)
                        .eq(SystemRole::getStatus, CommonStatusEnum.ENABLE.getStatus())
                        .eq(SystemRole::getDeleted, IsDeleteEnum.NORMAL.getType());
                    roleIds.addAll(systemRoleMapper.selectList(wrapper).stream().map(SystemRole::getId).collect(Collectors.toList()));
                }
                systemUserDto.setRoleIds(roleIds);
            }
            addSystemUser(null, systemUserDto);
        }

    }


    /**
     * 判断是否有重复数据
     *
     * @param systemUserDto
     */
    public void checkDuplicateData(SystemUserDto systemUserDto) {
        //用户名称
        if (!StringUtils.isEmpty(systemUserDto.getUsername())) {
            List<SystemUsers> systemUsersList = systemUsersMapper.selectList(new LambdaQueryWrapper<SystemUsers>()
                .eq(SystemUsers::getUsername, systemUserDto.getUsername())
                .eq(SystemUsers::getDeleted, IsDeleteEnum.NORMAL.getType()));
            if (!CollectionUtils.isEmpty(systemUsersList)) {
                throw new StatusException("用户名重复");
            }
        }
    }

    /**
     * 校验用户是否存在
     *
     * @param id
     */
    public void validateUserExists(String id) {
        LambdaQueryWrapper<SystemUsers> queryWrapper = new LambdaQueryWrapper<SystemUsers>()
            .eq(SystemUsers::getId, id)
            .eq(SystemUsers::getDeleted, IsDeleteEnum.NORMAL.getType());
        SystemUsers user = systemUsersMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new StatusException("用户不存在");
        }
    }

    /**
     * 查询用户与角色的绑定关系列表
     */
    public List<String> getUserRoleIds(String id) {
        LambdaQueryWrapper<SystemUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUserRole::getUserId, id);
        lambdaQueryWrapper.eq(SystemUserRole::getDeleted, IsDeleteEnum.NORMAL.getType());
        List<SystemUserRole> userRoleList = systemUserRoleMapper.selectList(lambdaQueryWrapper);
        List<String> userRoleIds = userRoleList.stream()
            .map(SystemUserRole::getId)
            .distinct()
            .collect(Collectors.toList());
        return userRoleIds;
    }

    /**
     * 查询用户与岗位的绑定关系列表
     */
    public List<String> getUserPostIds(String id) {
        LambdaQueryWrapper<SystemUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<SystemUserPost>()
            .eq(SystemUserPost::getUserId, id)
            .eq(SystemUserPost::getDeleted, IsDeleteEnum.NORMAL.getType());
        List<SystemUserPost> userPosts = systemUserPostMapper.selectList(lambdaQueryWrapper);
        List<String> userPostIds = userPosts.stream()
            .map(SystemUserPost::getId)
            .distinct()
            .collect(Collectors.toList());
        return userPostIds;
    }

    /**
     * 查询用户与部门绑定关系列表
     */
    public List<String> getUserDeptIds(String id) {
        LambdaQueryWrapper<SystemUserDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUserDept::getUserId, id);
        List<SystemUserDept> userDeptList = systemUserDeptMapper.selectList(lambdaQueryWrapper);
        List<String> userDeptIds = userDeptList.stream()
            .map(SystemUserDept::getId)
            .distinct()
            .collect(Collectors.toList());
        return userDeptIds;
    }

    public List<SystemDept> getDeptLists(SystemDept dept, List<SystemDept> systemDepts) {
        List<SystemDept> children = new ArrayList<>();
        for (SystemDept systemDept : systemDepts) {
            if (systemDept.getParentId().equals(dept.getId())) {
                children.add(systemDept);
                children.addAll(getDeptLists(systemDept, systemDepts));
            }
        }
        return children;
    }


    public SystemUsers getLoginUser() {
        //获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        LambdaQueryWrapper<SystemUsers> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUsers::getUsername, username)
            .eq(SystemUsers::getDeleted, IsDeleteEnum.NORMAL.getType());
        return systemUsersMapper.selectOne(wrapper);
    }


}
