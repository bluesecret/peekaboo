package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.RequestUtil;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRoleDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRoleMenuDTO;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemRolePermissionDTO;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuView;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemRoleVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemUsersView;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.RoleCodeEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemMenu;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemRole;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemRoleMenu;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemUserRole;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemMenuMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemRoleMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemRoleMenuMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemUserRoleMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemRoleService;
import cn.ruixi.azure.rainbow.boot.security.core.util.SecurityFrameworkUtils;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: bijie
 * @Date: 2024-01-08 16:01
 */
@Service
@Slf4j
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {

    @Resource
    private SystemRoleMapper systemRoleMapper;

    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;

    @Resource
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private RequestUtil requestUtil;
    @Override
    public Pages getSystemRoleList(SystemRoleDto systemRoleDto) {
        //获取角色列表
        LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemRole::getDeleted,IsDeleteEnum.NORMAL.getType());
        lambdaQueryWrapper.like(StringUtils.hasText(systemRoleDto.getName()),SystemRole::getName,systemRoleDto.getName());
        lambdaQueryWrapper.like(StringUtils.hasText(systemRoleDto.getCode()),SystemRole::getCode,systemRoleDto.getCode());
        lambdaQueryWrapper.eq(Objects.nonNull(systemRoleDto.getStatus()),SystemRole::getStatus,systemRoleDto.getStatus()+"");
        lambdaQueryWrapper.orderByAsc(SystemRole::getSort);
        Page<SystemRole> page = new Page<>(systemRoleDto.getCurrentPage(), systemRoleDto.getPageSize());
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(page, lambdaQueryWrapper);
        long total = page.getTotal();
        log.info("用户列表为{}"+systemRoleList);
        List<SystemRoleVo> systemRoleVos = BeanUtils.toBeanList(systemRoleList, SystemRoleVo.class);
        return  new Pages(systemRoleVos,total);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystemRoleById(String id) {
        String[] split = id.split(",");
        for (String str : split) {
            //删除角色的同时还需要删除对应的绑定关系
            //校验角色是否存在
            validateRoleExists(str);
            //删除角色
            SystemRole role = SystemRole.builder().id(str).deleted(IsDeleteEnum.DISABLE.getType()).build();
            systemRoleMapper.updateById(role);
            //删除角色菜单绑定关系
            List<String> roleMenuIds = getRoleMenuIds(str);
            if (!CollectionUtils.isEmpty(roleMenuIds)){
                systemRoleMenuMapper.deleteBatchIds(roleMenuIds);
            }
            //删除角色用户绑定关系
            LambdaQueryWrapper<SystemUserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemUserRole::getRoleId,id);
            systemUserRoleMapper.delete(queryWrapper);
        }

    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSystemRole(SystemRoleDto systemRoleDto) {
        //判断是否有重复数据
        checkDuplicateData(systemRoleDto);
        //添加角色
        SystemRole systemRole = BeanUtils.toBean(systemRoleDto, SystemRole.class);
        List<String> dataScopeDeptIds = systemRoleDto.getDataScopeDeptIds();

        if (dataScopeDeptIds!=null) {
            systemRole.setDataScopeDeptIds(dataScopeDeptIds.stream().collect(Collectors.joining(",")));
        }
        systemRole.setDeleted(IsDeleteEnum.NORMAL.getType());
        systemRole.setId(IdUtils.create());
        int insert = systemRoleMapper.insert(systemRole);
        if (insert>0){
            LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemRole::getName,systemRoleDto.getName());
            lambdaQueryWrapper.eq(SystemRole::getCode,systemRoleDto.getCode());
            lambdaQueryWrapper.eq(SystemRole::getDeleted,IsDeleteEnum.NORMAL.getType());
            //获取角色id
            String id = systemRoleMapper.selectOne(lambdaQueryWrapper).getId();
            List<SystemRoleMenu> systemRoleMenus=new ArrayList<>();
            if (!CollectionUtils.isEmpty(systemRoleDto.getMenuList())){
                for (String menuId : systemRoleDto.getMenuList()) {
                    SystemRoleMenu systemRoleMenu=new SystemRoleMenu();
                    systemRoleMenu.setId(IdUtils.create());
                    systemRoleMenu.setRoleId(id);
                    systemRoleMenu.setMenuId(menuId);
                    systemRoleMenu.setDeleted(IsDeleteEnum.NORMAL.getType());
                    systemRoleMenu.setCreator(SecurityFrameworkUtils.getRainbowUsername());
                    systemRoleMenus.add(systemRoleMenu);
                }
                //批量添加
                Db.saveBatch(systemRoleMenus);
            }
        }
    }

    @Override
    public SystemRoleVo getSystemRoleDetails(String id) {
        SystemRole systemRole = systemRoleMapper.selectById(id);
        //获取角色所对应菜单
        LambdaQueryWrapper<SystemRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemRoleMenu::getRoleId,id);
        List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(lambdaQueryWrapper);
        List<String> menuIds = systemRoleMenus.stream()
                .map(SystemRoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        SystemRoleVo roleVo = BeanUtils.toBean(systemRole, SystemRoleVo.class);
        roleVo.setMenuIds(menuIds);
        if (!StringUtils.isEmpty(systemRole.getDataScopeDeptIds())){
            List<String> list = Arrays.asList(systemRole.getDataScopeDeptIds().split(","));
            roleVo.setDataScopeDeptIds(list);
        }else {
            roleVo.setDataScopeDeptIds(Collections.emptyList());
        }
        //获取菜单列表
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMenu::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemMenu> menuList = systemMenuMapper.selectList(queryWrapper);
        List<SystemMenuVo> systemMenuVoList = BeanUtils.toBeanList(menuList, SystemMenuVo.class);
        roleVo.setSystemMenuVoList(systemMenuVoList);
        return roleVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleStatus(SystemRoleDto systemRoleDto) {
        //校验角色是否存在
        validateRoleExists(systemRoleDto.getId());
        SystemRole systemRole = BeanUtils.toBean(systemRoleDto, SystemRole.class);
        checkDeptStatus(systemRoleDto);
        systemRoleMapper.updateById(systemRole);
    }

    private void checkDeptStatus(SystemRoleDto systemRoleDto) {
        LambdaQueryWrapper<SystemUserRole> queryWrapper = new LambdaQueryWrapper<SystemUserRole>()
                .eq(SystemUserRole::getRoleId, systemRoleDto.getId());
        SystemUserRole systemUserRole = new SystemUserRole();
        //判断如果状态==1则修改关联表关系
        if (systemRoleDto.getStatus().equals(CommonStatusEnum.DISABLE.getStatus()) ){
            systemUserRole.setStatus("1");
            systemUserRoleMapper.update(systemUserRole, queryWrapper);
        }else {
            systemUserRole.setStatus("0");
            systemUserRoleMapper.update(systemUserRole, queryWrapper);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemRole(SystemRoleDto systemRoleDto) {
        //校验角色是否存在
        validateRoleExists(systemRoleDto.getId());
        //校验该角色是否能被更新
        checkToUpdate(systemRoleDto.getId());

        SystemRole systemRole1 = systemRoleMapper.selectById(systemRoleDto.getId());
        if (!Objects.equals(systemRoleDto.getCode(), systemRole1.getCode())){
            checkDuplicateData(systemRoleDto);
        }
        SystemRole systemRole = BeanUtils.toBean(systemRoleDto, SystemRole.class);
        //修改部门列表
        List<String> dataScopeDeptIds = systemRoleDto.getDataScopeDeptIds();
        if (!Objects.isNull(dataScopeDeptIds)){
            StringJoiner builder = new StringJoiner(",");
            dataScopeDeptIds.stream().forEach(aLong -> builder.add(aLong.toString()));
            systemRole.setDataScopeDeptIds(builder.toString());
        }
        //修改菜单  先全部删除在进行添加
        if (!Objects.isNull(systemRoleDto.getMenuList())){
            //先获取该角色所对应的菜单
            LambdaQueryWrapper<SystemRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemRoleMenu::getRoleId,systemRoleDto.getId());
            lambdaQueryWrapper.eq(SystemRoleMenu::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemRoleMenu> roleMenus = systemRoleMenuMapper.selectList(lambdaQueryWrapper);
            List<String> roleMenuIds = roleMenus.stream()
                    .map(SystemRoleMenu::getId)
                    .distinct()
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(roleMenuIds)){
                systemRoleMenuMapper.deleteBatchIds(roleMenuIds);
            }
            if (!Objects.isNull(systemRoleDto.getMenuList())){
                //添加关系
                List<SystemRoleMenu> systemRoleMenus= new ArrayList<>();
                for (String menuIds : systemRoleDto.getMenuList()) {
                    SystemRoleMenu sysRoleMenus=new SystemRoleMenu();
                    sysRoleMenus.setDeleted(IsDeleteEnum.NORMAL.getType());
                    sysRoleMenus.setRoleId(systemRoleDto.getId());
                    sysRoleMenus.setMenuId(menuIds);
                    sysRoleMenus.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    sysRoleMenus.setCreator(SecurityFrameworkUtils.getRainbowUsername());
                    systemRoleMenus.add(sysRoleMenus);
                }
                //批量添加
                for (SystemRoleMenu systemRoleMenu : systemRoleMenus) {
                    systemRoleMenuMapper.insert(systemRoleMenu);
                }
            }

        }
        systemRoleMapper.updateById(systemRole);
    }


    /**
     *是否包含管理员
     */
    @Override
    public boolean hasAnySuperAdmin(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)){
            return false;
        }
        //获取角色列表
        LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemRole::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(lambdaQueryWrapper);
        boolean result = systemRoleList.stream()
                .map(SystemRole::getCode)
                .anyMatch(s -> s.equals(RoleCodeEnum.SUPER_ADMIN.getCode()));
        return result;
    }

    @Override
    public List<SystemRoleVo> getRoleList() {
        LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<SystemRole>()
                .eq(SystemRole::getDeleted, IsDeleteEnum.NORMAL.getType()).eq(SystemRole::getStatus, CommonStatusEnum.ENABLE.getStatus());
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(lambdaQueryWrapper);
        List<SystemRoleVo> systemRoleVoList = BeanUtils.toBeanList(systemRoleList, SystemRoleVo.class);
        return systemRoleVoList;
    }

    /**
     * 获取指定角色ID列表的角色菜单列表。
     *
     * @param ids 角色ID的列表，用于查询相应的角色菜单关系。
     * @return 返回一个系统角色菜单数据传输对象（SystemRoleMenuDTO）的列表，这个列表只包含父级菜单ID为0的菜单项。
     */
    @Override
    public List<SystemRoleMenuDTO> getRoleMenuList(List<String> ids) {
        // 从数据库中根据角色ID列表查询所有的角色菜单关系
        List<SystemRoleMenuDTO> roleMenuList = systemRoleMenuMapper.selectRoleMenuList(ids);

        // 筛选出父级菜单ID为0的菜单项，并转换成相应的菜单信息DTO，最后收集到一个列表中返回
        List<SystemRoleMenuDTO> result = roleMenuList.stream()
                .filter(menu -> "0".equals(menu.getParentId())) // 筛选父级菜单
                .map(menu -> coverttMenuInfoDTO(menu, roleMenuList)) // 转换菜单信息
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 将单个菜单信息转换为具有子菜单的树状结构。
     *
     * @param menu 单个菜单信息，作为树结构的根节点或某个节点。
     * @param menuList 所有菜单信息的列表，用于查找当前菜单的子菜单。
     * @return 转换后的菜单树结构，其中包含了子菜单。
     */
    public SystemRoleMenuDTO coverttMenuInfoDTO(SystemRoleMenuDTO menu, List<SystemRoleMenuDTO> menuList) {
        SystemRoleMenuDTO node = new SystemRoleMenuDTO();
        BeanUtils.copyProperties(menu, node); // 复制menu的属性到新创建的node对象

        // 筛选出当前菜单的子菜单，并递归地将它们转换为树状结构
        List<SystemRoleMenuDTO> children = menuList.stream()
                .filter(subMenu -> (subMenu.getParentId().equals(menu.getId())))
                .map(subPermission -> coverttMenuInfoDTO(subPermission, menuList))
                .collect(Collectors.toList());
        node.setChildren(children); // 将子菜单列表设置到node对象中

        return node; // 返回转换后的菜单节点
    }

    /**
     * 获取指定角色ID列表的菜单权限列表。
     *
     * @param ids 角色ID的列表，用于查询相应的菜单权限。
     * @return 返回一个包含系统角色菜单权限信息的列表。
     */
    @Override
    public List<SystemRolePermissionDTO> getRoleMenuListAuthKey(List<String> ids) {
        // 通过角色ID列表查询角色对应的菜单权限
        return systemRoleMenuMapper.selectRoleMenuPermission(ids);
    }

    /**
     * 根据用户ID获取其角色列表。
     *
     * @param id 用户的唯一标识符。
     * @return 返回一个包含用户所有角色名称的字符串集合。
     */
    @Override
    public Set<String> getUserRoleList(String id) {
        // 通过systemUserRoleMapper获取指定用户的角色列表
        return systemUserRoleMapper.getUserRoleList(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRole(SystemRoleDto systemRoleDto) {
        if (systemRoleDto.getUserList().size()==0){
            throw new RuntimeException("请选择用户");
        }
        //当前登录用户
        String rainbowUsername = SecurityFrameworkUtils.getRainbowUsername();

        //先删除
        LambdaQueryWrapper<SystemUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserRole::getRoleId,systemRoleDto.getId());
        systemUserRoleMapper.delete(queryWrapper);
        //新增
        ArrayList<SystemUserRole> systemUserRoles = new ArrayList<>();
        systemRoleDto.getUserList().forEach(data->{
            SystemUserRole systemUserRole = new SystemUserRole();
            systemUserRole.setRoleId(systemRoleDto.getId());
            systemUserRole.setId(IdUtils.create());
            systemUserRole.setUserId(data);
            systemUserRole.setCreateTime(new Timestamp(System.currentTimeMillis()));
            systemUserRole.setCreator(rainbowUsername);
            systemUserRole.setDeleted(IsDeleteEnum.NORMAL.getType());
            systemUserRoles.add(systemUserRole);
        });

        Db.saveBatch(systemUserRoles);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRoleMenu(SystemRoleDto systemRoleDto) {
        if (systemRoleDto.getMenuList().size()==0){
            throw new RuntimeException("请选择菜单");
        }
        //当前登录用户
        String rainbowUsername = SecurityFrameworkUtils.getRainbowUsername();
        LambdaQueryWrapper<SystemRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemRoleMenu::getRoleId,systemRoleDto.getId());
        systemRoleMenuMapper.delete(queryWrapper);
        //新增
        ArrayList<SystemRoleMenu> systemRoleMenus = new ArrayList<>();
        systemRoleDto.getMenuList().forEach(data->{
            SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
            systemRoleMenu.setRoleId(systemRoleDto.getId());
            systemRoleMenu.setId(IdUtils.create());
            systemRoleMenu.setMenuId(data);
            systemRoleMenu.setCreateTime(new Timestamp(System.currentTimeMillis()));
            systemRoleMenu.setCreator(rainbowUsername);
            systemRoleMenu.setDeleted(IsDeleteEnum.NORMAL.getType());
            systemRoleMenus.add(systemRoleMenu);
        });

        Db.saveBatch(systemRoleMenus);
    }

    @Override
    public List<SystemUsersView>  getBindUser(String id) {
        //获取已经绑定的用户列表
        List<SystemUsersView> systemUsersViewList = systemUserRoleMapper.getBindUser(id);
        //拼接url请求
        systemUsersViewList.forEach(data->{
            data.setAvatar(requestUtil.getUrl() +data.getAvatar());
        });
        return systemUsersViewList;
    }

    @Override
    public List<SystemMenuView> getBindMenu(String id) {

        return systemRoleMenuMapper.getBindMenu(id);

    }




    /**
     * 校验角色是否存在
     * @param id
     */
    public void validateRoleExists(String id) {
        LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<SystemRole>()
                .eq(SystemRole::getId,id)
                .eq(SystemRole::getDeleted, IsDeleteEnum.NORMAL.getType());
        SystemRole systemRole = systemRoleMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(systemRole)){
            throw new BusinessException("角色不存在");
        }
    }

    /**
     * 获取角色与菜单的关系绑定列表
     */
    public List<String> getRoleMenuIds(String id){
        LambdaQueryWrapper<SystemRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemRoleMenu::getRoleId,id);
        lambdaQueryWrapper.eq(SystemRoleMenu::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(lambdaQueryWrapper);
        List<String> roleMenuIds = systemRoleMenus.stream()
                .map(SystemRoleMenu::getId)
                .distinct()
                .collect(Collectors.toList());
        return roleMenuIds;
    }

    /**
     * 判断是否有重复数据
     */
    public void checkDuplicateData(SystemRoleDto systemRoleDto){

        //校验角色编码
        if (!StringUtils.isEmpty(systemRoleDto.getCode())){
            LambdaQueryWrapper<SystemRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemRole::getDeleted,IsDeleteEnum.NORMAL.getType());
            lambdaQueryWrapper.eq(SystemRole::getCode,systemRoleDto.getCode());
            List<SystemRole> systemRoleList = systemRoleMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemRoleList)){
                throw new BusinessException("角色标识重复");
            }
        }
    }
    /**

    /**
     * 校验该角色是否能被更新
     */
    public void  checkToUpdate(String id){
        //校验角色是否存在
        validateRoleExists(id);
//        //如果是系统内置角色是不能被更新的
//        SystemRole systemRole = systemRoleMapper.selectById(id);
//        if (RoleTypeEnum.SYSTEM.getType().equals(systemRole.getType())){
//            throw new BusinessException("系统内置角色不能更新");
//        }
    }


    /**
     * 获取当前菜单下面的所有子菜单
     */
    public List<SystemMenu> getMenusList(SystemMenu menu, List<SystemMenu> systemMenuList){
        List<SystemMenu> children=new ArrayList<>();
        for (SystemMenu systemMenu : systemMenuList) {
            if (systemMenu.getParentId().equals(menu.getId())){
                children.add(systemMenu);
                children.addAll(getMenusList(systemMenu,systemMenuList));
            }
        }
        return children;
    }


}
