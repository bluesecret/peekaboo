package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Constants;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDeptDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptTreeDetailVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptTreeVo;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDeptVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDept;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemUserDept;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemUsers;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemDeptMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemUserDeptMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemUsersMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDepartmentService;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: bijie
 * @Date: 2024-01-04 11:17
 */
@Service
@Slf4j
public class SystemDepartmentServiceImpl extends ServiceImpl<SystemDeptMapper, SystemDept> implements SystemDepartmentService {

    @Resource
    private SystemDeptMapper sysDepartmentDao;

    @Resource
    private SystemUsersMapper sysUserDao;

    @Autowired
    private SystemUserDeptMapper systemUserDeptMapper;


    @Override
    public void  addDepartment(SystemDeptDto systemDeptDto) {
        SystemDept sysDepartment = BeanUtils.toBean(systemDeptDto, SystemDept.class);
        sysDepartment.setId(IdUtils.create());
        if (StringUtils.isEmpty(sysDepartment.getParentId())){
            sysDepartment.setParentId(Constants.DEFAULT_PARENT_ID);
        }
        //校验部门名字
        validateDeptNameUnique(systemDeptDto);
        sysDepartment.setDeleted(IsDeleteEnum.NORMAL.getType());
        sysDepartmentDao.insert(sysDepartment);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(String id) {
        String[] strings = id.split(",");
        for (String string : strings) {
            //校验部门是否存在
            validateDeptExists(string);
            //查询当前部门下用户
            List<SystemUserDept> systemUserDepts = sysDepartmentDao.selectDeptUserList(string);
            LambdaQueryWrapper<SystemDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemDept::getParentId,string);
            lambdaQueryWrapper.eq(SystemDept::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemDept> systemDepts = sysDepartmentDao.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemDepts)){
                throw new BusinessException("该部门存在子部门");
            }
            if (!CollectionUtils.isEmpty(systemUserDepts)){
                //删除关联关系
                systemUserDeptMapper.deleteBatchIds(systemUserDepts.stream().map(SystemUserDept::getId).collect(Collectors.toList()));
            }
            SystemDept systemDept = SystemDept.builder().id(string).deleted(IsDeleteEnum.DISABLE.getType()).build();
            sysDepartmentDao.updateById(systemDept);
            //删除关联信息表
            sysDepartmentDao.deleteDeptUserList(string);

        }
    }

    @Override
    public List<SystemDeptTreeVo> getSysDepartmentTreeList() {
        List<SystemDeptTreeVo> systemDeptTreeVos = sysDepartmentDao.selectDeptTreeList();
        return systemDeptTreeVos.stream()
                .filter(menu -> "0".equals(menu.getParentId())) // 筛选父级菜单
                .map(menu -> coverttMenuInfoDTO(menu, systemDeptTreeVos)) // 转换菜单信息
                .collect(Collectors.toList());
    }


    public SystemDeptTreeVo coverttMenuInfoDTO(SystemDeptTreeVo menu, List<SystemDeptTreeVo> menuList) {
        SystemDeptTreeVo node = new SystemDeptTreeVo();
        BeanUtils.copyProperties(menu, node); // 复制menu的属性到新创建的node对象

        // 筛选出当前菜单的子菜单，并递归地将它们转换为树状结构
        List<SystemDeptTreeVo> children = menuList.stream()
                .filter(subMenu -> (subMenu.getParentId().equals(menu.getId())))
                .map(subPermission -> coverttMenuInfoDTO(subPermission, menuList))
                .collect(Collectors.toList());
        node.setChildren(children); // 将子菜单列表设置到node对象中
        return node; // 返回转换后的菜单节点
    }

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<SystemDeptVo> getSysDepartmentList( ) {
        List<SystemDept> systemDeptList = sysDepartmentDao.selectList(new LambdaQueryWrapper<SystemDept>().eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType())
        .eq(SystemDept::getStatus, CommonStatusEnum.DISABLE.getStatus()));
        List<SystemDeptVo> systemDeptVoList = BeanUtils.toBeanList(systemDeptList, SystemDeptVo.class);
        for (SystemDeptVo systemDeptVo : systemDeptVoList) {
            systemDeptVo.setKey(systemDeptVo.getId().toString());
            systemDeptVo.setValue(systemDeptVo.getName());
            systemDeptVo.setTitle(systemDeptVo.getName());
        }
        return systemDeptVoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadSysDepartment(SystemDeptDto sysDepartmentDto) {
        //校验部门是否存在
        validateDeptExists(sysDepartmentDto.getId());
        //校验部门代码是否重复
        SystemDept systemDept = sysDepartmentDao.selectById(sysDepartmentDto.getId());
        if (Strings.isNotBlank(sysDepartmentDto.getCode())&&!Objects.equals(systemDept.getCode(), sysDepartmentDto.getCode())){
            validateDeptCodeUnique(sysDepartmentDto);
        }
        SystemDept department = BeanUtils.toBean(sysDepartmentDto, SystemDept.class);
        //编辑父部门时不能选本身或者本身子模块
        validateParentDeptExists(sysDepartmentDto);

        //如果部门状态修改 则关联表关系也需要修改
        checkDeptStatus(sysDepartmentDto);
        sysDepartmentDao.updateById(department);
    }

    private void checkDeptStatus(SystemDeptDto sysDepartmentDto) {
        //递归获取当前节点id下的所有子节点
        if (sysDepartmentDto.getStatus()!=null){
            SystemDept systemDept = BeanUtils.toBean(sysDepartmentDto, SystemDept.class);
            List<SystemDept> systemDeptList = sysDepartmentDao.selectList(new LambdaQueryWrapper<SystemDept>().eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType()));
            List<SystemDept> deptList = getDeptList(systemDept, systemDeptList);
            if (sysDepartmentDto.getStatus()==CommonStatusEnum.DISABLE.getStatus()) {
                for (SystemDept dept : deptList) {
                    LambdaQueryWrapper<SystemUserDept> queryWrapper = new LambdaQueryWrapper<SystemUserDept>()
                            .eq(SystemUserDept::getDeptId, dept.getId());
                    SystemUserDept systemUserDept = new SystemUserDept();
                    systemUserDept.setStatus(systemDept.getStatus().toString());
                    systemUserDeptMapper.update(systemUserDept, queryWrapper);
                    dept.setStatus(sysDepartmentDto.getStatus());
                    sysDepartmentDao.updateById(dept);

                }
            }
        }
    }



    //校验部门代码是否重复
    private void validateDeptCodeUnique(SystemDeptDto systemDeptDto) {
        LambdaQueryWrapper<SystemDept> queryWrapper = new LambdaQueryWrapper<SystemDept>()
                .eq(SystemDept::getCode, systemDeptDto.getCode())
                .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType());
        List<SystemDept> systemDepts = sysDepartmentDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(systemDepts)){
            throw new BusinessException("部门代码重复");
        }
    }
    @Override
    public void updateDeptLeaders(SystemDeptDto systemDeptDto) {
        //校验部门是否存在
        validateDeptExists(systemDeptDto.getId());
        SystemDept systemDept = BeanUtils.toBean(systemDeptDto, SystemDept.class);
        sysDepartmentDao.updateById(systemDept);
    }

    @Override
    public List<SystemDeptTreeDetailVo> getSysDepartmentDetailList(String name, String status) {
        if (Strings.isNotBlank(name)){
            name="%"+name+"%";
        }
        List<SystemDeptTreeDetailVo> systemDeptTreeDetailVos = sysDepartmentDao.selectDeptTreeDetailList(name,status);

        List<SystemDeptTreeDetailVo> collect = systemDeptTreeDetailVos.stream()
                .filter(menu -> "0".equals(menu.getParentId())) // 筛选父级菜单
                .map(menu -> coverttMenuInfoDTO1(menu, systemDeptTreeDetailVos)) // 转换菜单信息
                .collect(Collectors.toList());
        //如果是空则不需要生成树
        if (CollectionUtils.isEmpty(collect)){
            return systemDeptTreeDetailVos;
        }
        return  collect;


    }


    /**
     * 根据子节点获取最上层节点
     * @param deptAll 所有部门集合
     * @param deptChild 子节点
     * @return
     */
    public static SystemDeptTreeDetailVo getMaximumParent(List<SystemDeptTreeDetailVo> deptAll, SystemDeptTreeDetailVo deptChild){
        SystemDeptTreeDetailVo dept = null;
        String parentId = deptChild.getParentId();
        if(parentId.equals("0")){
            dept = deptChild;
        }else {
            List<SystemDeptTreeDetailVo> parent = deptAll.stream().filter(item -> item.getId().equals(parentId)).collect(Collectors.toList());
            SystemDeptTreeDetailVo maximumParent = getMaximumParent(deptAll, parent.get(0));
            dept = maximumParent;
        }
        return dept;
    }
    @Override
    public int updateSystemDeptStatus(SystemDeptDto systemDeptDto) {
        return sysDepartmentDao.updateById(BeanUtils.toBean(systemDeptDto, SystemDept.class));
    }

    @Override
    public SystemDeptDto findById(String id) {
        SystemDept systemDept = sysDepartmentDao.selectById(id);
        if (systemDept == null) {
            throw new BusinessException("部门不存在");
        }
        SystemDeptDto systemDeptDto;
        String leaderUserId = systemDept.getLeaderUserId();
        if (Strings.isNotBlank(leaderUserId)) {
            LambdaQueryWrapper<SystemUsers> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemUsers::getId, leaderUserId);

            Optional<SystemUsers> optionalSystemUser = Optional.ofNullable(sysUserDao.selectOne(wrapper));
            systemDeptDto = BeanUtils.toBean(systemDept, SystemDeptDto.class);
            optionalSystemUser.ifPresent(user -> systemDeptDto.setLeaderName(user.getNickname()));
        } else {
            systemDeptDto = BeanUtils.toBean(systemDept, SystemDeptDto.class);
            systemDeptDto.setLeaderName(null); // 使用默认值
        }

        return systemDeptDto;
    }

    @Override
    public Page getPageSysDeptTreeDetailList(String name, String status, Integer pageNo, Integer pageSize) {
        Page page = new Page<>(pageNo,pageSize);
        LambdaQueryWrapper<SystemDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemDept::getParentId,"0").eq(SystemDept::getDeleted,IsDeleteEnum.NORMAL.getType()).orderByAsc(SystemDept::getSort);
        List<SystemDept> systemDepts = sysDepartmentDao.selectList(page, wrapper);

        List<SystemDeptTreeDetailVo> pageTree = getChild(BeanUtils.toBeanList(systemDepts, SystemDeptTreeDetailVo.class));
        return page.setRecords(pageTree);
    }

    @Override
    public List<SystemDeptTreeVo> getSysDepartmentListStatus() {
        List<SystemDeptTreeVo> systemDeptTreeVos = sysDepartmentDao.selectDeptTreeListStatus();
        return systemDeptTreeVos.stream()
                .filter(menu -> "0".equals(menu.getParentId())) // 筛选父级菜单
                .map(menu -> coverttMenuInfoDTO(menu, systemDeptTreeVos)) // 转换菜单信息
                .collect(Collectors.toList());
    }

    private List<SystemDeptTreeDetailVo> getChild(List<SystemDeptTreeDetailVo> list){
                //分别遍历每个顶级父类
        for (SystemDeptTreeDetailVo entry : list) {
            LambdaQueryWrapper<SystemDept> wrapper = new LambdaQueryWrapper<>();
            String id = entry.getId();//顶级父类的id

            wrapper.eq(SystemDept::getParentId,id);
            List<SystemDept> childs = sysDepartmentDao.selectList(wrapper);
            List<SystemDeptTreeDetailVo> systemDeptTreeDetailVos = BeanUtils.toBeanList(childs, SystemDeptTreeDetailVo.class);
            //父id=id，就表示是id的孩子，从而获取到id的所有孩子

            //做一个判断，符合有孩子条件就进入继续递归，就像链表一样把路径上所有孩子遍历完再递归setChilds
            if (childs != null && childs.size() != 0) {
                getChild(systemDeptTreeDetailVos);
                entry.setChildren(systemDeptTreeDetailVos);
            }
        }
        return list;
    }

    public SystemDeptTreeDetailVo coverttMenuInfoDTO1(SystemDeptTreeDetailVo menu, List<SystemDeptTreeDetailVo> menuList) {
        SystemDeptTreeDetailVo node = new SystemDeptTreeDetailVo();
        BeanUtils.copyProperties(menu, node); // 复制menu的属性到新创建的node对象

        // 筛选出当前菜单的子菜单，并递归地将它们转换为树状结构
        List<SystemDeptTreeDetailVo> children = menuList.stream()
                .filter(subMenu -> (subMenu.getParentId().equals(menu.getId())))
                .map(subPermission -> coverttMenuInfoDTO1(subPermission, menuList))
                .collect(Collectors.toList());
        node.setChildren(children); // 将子菜单列表设置到node对象中
        return node; // 返回转换后的菜单节点
    }

    /**
     * 查询子模块
     * @return
     */
    private List<SystemDeptVo> getChildren(SystemDeptVo parent, List<SystemDeptVo> list) {
        List<SystemDeptVo> sysDepartmentVos = list.stream()
                .filter(new Predicate<SystemDeptVo>() {
                    @Override
                    public boolean test(SystemDeptVo sysDepartmentVo) {
                        return sysDepartmentVo.getParentId().equals(parent.getId());
                    }
                })
                .map(sysDepartmentVo -> {
                    sysDepartmentVo.setChildren(getChildren(sysDepartmentVo, list));
                    sysDepartmentVo.setKey(sysDepartmentVo.getId());
                    sysDepartmentVo.setTitle(sysDepartmentVo.getName());
                    sysDepartmentVo.setValue(sysDepartmentVo.getId());
                    return sysDepartmentVo;
                })
                .collect(Collectors.toList());
        return sysDepartmentVos;
    }

    /**
     * 校验部门名字
     */
    public void validateDeptNameUnique(SystemDeptDto systemDeptDto) {
        LambdaQueryWrapper<SystemDept> queryWrapper = new LambdaQueryWrapper<SystemDept>()
                .eq(SystemDept::getName, systemDeptDto.getName())
                .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType());
        List<SystemDept> systemDepts = sysDepartmentDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(systemDepts)){
            throw new BusinessException("部门已经存在");
        }
    }

    /**
     * 校验部门是否存在
     */
    public void  validateDeptExists(String id){
        LambdaQueryWrapper<SystemDept> lambdaQueryWrapper = new LambdaQueryWrapper<SystemDept>()
                .eq(SystemDept::getId, id)
                .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType());
        SystemDept systemDept = sysDepartmentDao.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(systemDept)){
            throw new BusinessException("部门不存在");
        }
    }

    /**
     * 编辑父部门时不能选本身或者本身子模块
     * @param sysDepartmentDto
     */
    public void validateParentDeptExists(SystemDeptDto sysDepartmentDto) {
        SystemDept systemDept = BeanUtils.toBean(sysDepartmentDto, SystemDept.class);
        //不能设置自己为父模块
        if (sysDepartmentDto.getId().equals(sysDepartmentDto.getParentId())){
            throw new BusinessException("不能设置自己为父模块");
        }
        //获取全部部门
        List<SystemDept> systemDeptList = sysDepartmentDao.selectList(new LambdaQueryWrapper<SystemDept>()
                .eq(SystemDept::getDeleted, IsDeleteEnum.NORMAL.getType()));
        List<SystemDept> deptList = getDeptList(systemDept, systemDeptList);
        List<String> ids = deptList.stream().map(SystemDept::getId).collect(Collectors.toList());
        if (ids.contains(sysDepartmentDto.getParentId())){
            throw new BusinessException("不能设置自己的子部门为父部门");
        }
    }

    /**
     * 获取当前部门下面的子部门
     */
    public List<SystemDept> getDeptList(SystemDept dept, List<SystemDept> systemDepts){
        List<SystemDept> children=new ArrayList<>();
        for (SystemDept systemDept : systemDepts) {
          if (systemDept.getParentId().equals(dept.getId())){
              children.add(systemDept);
              children.addAll(getDeptList(systemDept,systemDepts));
          }
        }
        return children;
    }
}
