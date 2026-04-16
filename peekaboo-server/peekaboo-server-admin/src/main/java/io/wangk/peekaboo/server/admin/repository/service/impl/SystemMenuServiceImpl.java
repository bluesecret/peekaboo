package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Constants;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemMenuDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuView;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemMenuVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.MenuTypeEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemMenu;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemRoleMenu;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemMenuMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemRoleMenuMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemMenuService;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: bijie
 * @Date: 2024-01-09 11:08
 */
@Service
@Slf4j
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {

    @Resource
    private SystemMenuMapper systemMenuMapper;

    @Resource
    private SystemRoleMenuMapper systemRoleMenuMapper;


    @Override
    public List<SystemMenuVo> getSystemMenuList(String name, String status, String visible) {
        //根据条件获取数据
        LambdaQueryWrapper<SystemMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenu::getDeleted, IsDeleteEnum.NORMAL.getType())
                .like(StringUtils.hasText(name), SystemMenu::getName, name)
                .eq(StringUtils.hasText(status), SystemMenu::getStatus, status)
                .eq(StringUtils.hasText(visible), SystemMenu::getVisible, visible)
                .orderByAsc(SystemMenu::getSort)
                .orderByDesc(SystemMenu::getCreateTime);
        List<SystemMenu> systemMenus = systemMenuMapper.selectList(wrapper);
        List<SystemMenuVo> systemMenuVoList = BeanUtils.toBeanList(systemMenus, SystemMenuVo.class);
        List<SystemMenuVo> systemMenuVos = systemMenuVoList.stream()
                .filter(systemMenuVo -> Objects.equals("0", systemMenuVo.getParentId()))
                .map(systemMenuVo -> {
                    systemMenuVo.setChildren(getChildren(systemMenuVo, systemMenuVoList));
                    systemMenuVo.setKey(systemMenuVo.getId());
                    systemMenuVo.setValue(systemMenuVo.getId());
                    systemMenuVo.setTitle(systemMenuVo.getName());
                    return systemMenuVo;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(systemMenuVos)){
            return systemMenuVoList;
        }
        return systemMenuVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystemMenu(String id) {
        String[] strings = id.split(",");
        for (String str : strings) {
            //校验菜单是否存在
            checkMenusIsExist(str);
            //删除菜单时先判断当前菜单有没有子菜单,有则提示不能删除
            //获取菜单列表
            List<SystemMenu> systemMenuList = getMenusList();
            //菜单目录下没有类型为目录的菜单可以删除
            List<SystemMenu> collect = systemMenuList.stream().filter(data -> data.getType() == 1).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)){
                List<SystemMenu> menus = collect.stream()
                        .filter(new Predicate<SystemMenu>() {
                            @Override
                            public boolean test(SystemMenu systemMenu1) {
                                return systemMenu1.getParentId().equals(str);
                            }
                        })
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(menus)){
                    throw new BusinessException("该菜单存在子目录,无法删除!");
                }
            }

            SystemMenu menu = SystemMenu.builder().id(str).deleted(IsDeleteEnum.DISABLE.getType()).build();
            systemMenuMapper.updateById(menu);
            //删除菜单的同时还需要将关系表中的一起删除掉
            LambdaQueryWrapper<SystemRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<SystemRoleMenu>()
                    .eq(SystemRoleMenu::getMenuId, str)
                    .eq(SystemRoleMenu::getDeleted, IsDeleteEnum.NORMAL.getType());
            List<SystemRoleMenu> systemRoleMenus = systemRoleMenuMapper.selectList(lambdaQueryWrapper);
            List<String> ids = systemRoleMenus.stream()
                    .map(SystemRoleMenu::getId)
                    .distinct()
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(ids)){
                //批量删除
                systemRoleMenuMapper.deleteBatchIds(ids);
            }
        }
    }

    @Override
    public SystemMenuView getSystemDetails(String id) {
        SystemMenu systemMenu = systemMenuMapper.selectById(id);
        SystemMenuView systemMenuView = BeanUtils.toBean(systemMenu, SystemMenuView.class);
        return systemMenuView;
    }

    @Override
    public void updateSystemMenu(SystemMenuDto systemMenuDto) {
        //校验父菜单
        checkParentMenu(systemMenuDto);
        SystemMenu menu = BeanUtils.toBean(systemMenuDto, SystemMenu.class);

//        if (StringUtils.isEmpty(systemMenuDto.getParentId())){
//            menu.setParentId(Constants.DEFAULT_PARENT_ID);
//        }
        checkMenuStatus(systemMenuDto);
        systemMenuMapper.updateById(menu);
    }

    private void checkMenuStatus(SystemMenuDto systemMenuDto) {

        if (Objects.nonNull(systemMenuDto.getStatus())){
            //递归获取当前节点id下的所有子节点
            SystemMenuVo systemMenuVo = BeanUtils.toBean(systemMenuDto, SystemMenuVo.class);
            List<SystemMenu> systemMenuList = systemMenuMapper.selectList(new LambdaQueryWrapper<SystemMenu>().eq(SystemMenu::getDeleted, IsDeleteEnum.NORMAL.getType()));
            List<SystemMenuVo> systemMenuVoList = BeanUtils.toBeanList(systemMenuList, SystemMenuVo.class);
            List<SystemMenuVo> systemMenuVos = getChildren(systemMenuVo, systemMenuVoList);
            if (systemMenuDto.getStatus()==CommonStatusEnum.DISABLE.getStatus()) {
                for (SystemMenuVo menuVo : systemMenuVos) {
                    LambdaQueryWrapper<SystemRoleMenu> queryWrapper = new LambdaQueryWrapper<SystemRoleMenu>()
                            .eq(SystemRoleMenu::getRoleId, menuVo.getId());

                    SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
                    systemRoleMenu.setStatus(systemMenuDto.getStatus().toString());
                    menuVo.setStatus(systemMenuDto.getStatus());
                    systemRoleMenuMapper.update(systemRoleMenu, queryWrapper);
                    systemMenuMapper.updateById(BeanUtils.toBean(menuVo, SystemMenu.class));
                }
            }
        }


    }


    @Override
    public void addSystemMenu(SystemMenuDto systemMenuDto) {
        //判断相同父编号下是否存在相同名字
        checkDuplicateData(systemMenuDto);
        //校验父菜单
        checkParentMenu(systemMenuDto);
        SystemMenu systemMenu = BeanUtils.toBean(systemMenuDto, SystemMenu.class);
        systemMenu.setId(IdUtils.create());
        //如果父id为空，设置默认值0
        if (Objects.isNull(systemMenuDto.getParentId())){
            systemMenu.setParentId(Constants.DEFAULT_PARENT_ID);
        }
        systemMenu.setDeleted(IsDeleteEnum.NORMAL.getType());
        systemMenuMapper.insert(systemMenu);
    }

    @Override
    public List<SystemMenuView> getMenuList() {
        List<SystemMenu> systemMenuList = getMenusList();
        List<SystemMenuView> systemMenuVoList = BeanUtils.toBeanList(systemMenuList, SystemMenuView.class);
        return systemMenuVoList;
    }

    @Override
    public List<SystemMenuVo> getMenuListTree() {
        //获取菜单列表
        List<SystemMenu> systemMenuList = getMenusList();
        List<SystemMenu> collect = systemMenuList.stream().filter(data -> data.getType() == 1).collect(Collectors.toList());
        List<SystemMenuVo> systemMenuVoList = BeanUtils.toBeanList(collect, SystemMenuVo.class);
        List<SystemMenuVo> systemMenuVos = systemMenuVoList.stream()
                .filter(systemMenuVo -> Objects.equals("0", systemMenuVo.getParentId()))
                .map(systemMenuVo -> {
                    systemMenuVo.setChildren(getChildren(systemMenuVo, systemMenuVoList));
                    systemMenuVo.setKey(systemMenuVo.getId());
                    systemMenuVo.setValue(systemMenuVo.getId());
                    systemMenuVo.setTitle(systemMenuVo.getName());
                    return systemMenuVo;
                })
                .collect(Collectors.toList());
        return systemMenuVos;
    }

    @Override
    public List<SystemMenuVo> getSystemMenuTreeStatusList() {

        //根据条件获取数据
        LambdaQueryWrapper<SystemMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenu::getDeleted, IsDeleteEnum.NORMAL.getType())
                .eq(SystemMenu::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(SystemMenu::getSort)
                .orderByDesc(SystemMenu::getCreateTime);
        List<SystemMenu> systemMenus = systemMenuMapper.selectList(wrapper);
        List<SystemMenuVo> systemMenuVoList = BeanUtils.toBeanList(systemMenus, SystemMenuVo.class);
        List<SystemMenuVo> systemMenuVos = systemMenuVoList.stream()
                .filter(systemMenuVo -> Objects.equals("0", systemMenuVo.getParentId()))
                .map(systemMenuVo -> {
                    systemMenuVo.setChildren(getChildren(systemMenuVo, systemMenuVoList));
                    systemMenuVo.setKey(systemMenuVo.getId());
                    systemMenuVo.setValue(systemMenuVo.getId());
                    systemMenuVo.setTitle(systemMenuVo.getName());
                    return systemMenuVo;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(systemMenuVos)){
            return systemMenuVoList;
        }
        return systemMenuVos;
    }


    /**
     * 获取当前菜单的所有父菜单
     * @param menu
     * @return
     */
    public List<SystemMenu> getParentMenus(SystemMenu menu, List<SystemMenu> menuList) {
        List<SystemMenu> children=new ArrayList<>();
        for (SystemMenu systemMenu : menuList) {
            if (systemMenu.getParentId().equals(menu.getId())){
                children.add(systemMenu);
                children.addAll(getParentMenus(systemMenu,menuList));
            }
        }
        return children;
    }

    /**
     * 获取子菜单
     * @param parent
     * @param systemMenuVoList
     * @return
     */
    private List<SystemMenuVo> getChildren(SystemMenuVo parent, List<SystemMenuVo> systemMenuVoList) {
        List<SystemMenuVo> systemMenuVos = systemMenuVoList.stream()
                .filter(systemMenuVo -> systemMenuVo.getParentId().equals(parent.getId()))
                .map(systemMenuVo -> {
                    systemMenuVo.setChildren(getChildren(systemMenuVo, systemMenuVoList));
                    systemMenuVo.setKey(systemMenuVo.getId().toString());
                    systemMenuVo.setValue(systemMenuVo.getId().toString());
                    systemMenuVo.setTitle(systemMenuVo.getName());
                    return systemMenuVo;
                })
                .collect(Collectors.toList());
        return systemMenuVos;
    }


    /**
     * 校验是否有重复数据
     */
    public void  checkDuplicateData(SystemMenuDto systemMenuDto){
        //校验菜单名称
        if (!StringUtils.isEmpty(systemMenuDto.getName())){
            LambdaQueryWrapper<SystemMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemMenu::getName,systemMenuDto.getName());
            lambdaQueryWrapper.eq(SystemMenu::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemMenu> systemMenuList = systemMenuMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemMenuList)){
                throw new BusinessException("菜单名字重复");
            }
        }
    }

    /**
     * 校验父菜单
     */
    public void checkParentMenu(SystemMenuDto systemMenuDto){
        //查询全部菜单
        List<SystemMenu> menusList = getMenusList();
        SystemMenu menu = BeanUtils.toBean(systemMenuDto, SystemMenu.class);
        if (!StringUtils.isEmpty(systemMenuDto.getParentId())){
            //修改的父菜单id必须不能包含本身或者子菜单
            List<SystemMenu> menus = getParentMenus(menu, menusList);
            menus.add(menu);
            log.info("子菜单列表为:{}"+menus);
            List<String> ids = menus.stream()
                    .map(SystemMenu::getId)
                    .distinct()
                    .collect(Collectors.toList());
            String parentId = systemMenuDto.getParentId();
            if (ids.contains(parentId)){
                throw new BusinessException("不能选自身或者子菜单为父菜单");
            }
            //父菜单类型必须是目录或者菜单类型
            SystemMenu systemMenu = systemMenuMapper.selectById(systemMenuDto.getParentId());
            if (Objects.nonNull(systemMenu)) {
                int systemMenuType = systemMenuDto.getType();
                int currentMenuType = systemMenu.getType();

                if (systemMenuType == 3) {
                    if ((currentMenuType == MenuTypeEnum.BUTTON.getType() || currentMenuType == MenuTypeEnum.DIR.getType())) {
                        throw new BusinessException("父菜单类型必须是菜单类型");
                    }
                }
                if (systemMenuType == 2) {
                    if ((currentMenuType == MenuTypeEnum.MENU.getType() || currentMenuType == MenuTypeEnum.BUTTON.getType())) {
                        throw new BusinessException("父菜单类型必须是目录类型");
                    }
                }
            }


        }
    }

    /**
     * 获取菜单列表
     */
    public List<SystemMenu> getMenusList(){
        LambdaQueryWrapper<SystemMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemMenu::getDeleted,IsDeleteEnum.NORMAL.getType())
        .orderByAsc(SystemMenu::getSort).orderByDesc(SystemMenu::getCreateTime);
        List<SystemMenu> systemMenuList = systemMenuMapper.selectList(lambdaQueryWrapper);
        return systemMenuList;
    }

    /**
     * 校验菜单是否存在
     */
    public void checkMenusIsExist(String id){
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<SystemMenu>()
                .eq(SystemMenu::getId, id)
                .eq(SystemMenu::getDeleted, IsDeleteEnum.NORMAL.getType());
        SystemMenu menu = systemMenuMapper.selectOne(queryWrapper);
        if (Objects.isNull(menu)){
            throw new BusinessException("菜单不存在");
        }
    }


}
