package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDictTypeDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.*;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDictData;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDictType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemDictDataMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemDictTypeMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDictTypeService;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: bijie
 * @Date: 2024-01-11 15:25
 */
@Service
@Slf4j
public class SystemDictTypeServiceImpl extends ServiceImpl<SystemDictTypeMapper, SystemDictType> implements SystemDictTypeService {

    @Resource
    private SystemDictTypeMapper systemDictTypeMapper;

    @Resource
    private SystemDictDataMapper systemDictDataMapper;
    @Override
    public Pages getSystemDictTypeList(SystemDictTypeDto systemDictTypeDto) {
        SystemDictType systemDictType = BeanUtils.toBean(systemDictTypeDto, SystemDictType.class);
        //获取字典类型列表
        LambdaQueryWrapper<SystemDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType())
        .eq(!Objects.isNull(systemDictType.getStatus()),SystemDictType::getStatus,systemDictType.getStatus());
        lambdaQueryWrapper.like(StringUtils.hasText(systemDictType.getName()),SystemDictType::getName,systemDictType.getName());
        lambdaQueryWrapper.like(StringUtils.hasText(systemDictType.getType()),SystemDictType::getType,systemDictType.getType());
        lambdaQueryWrapper.orderByDesc(SystemDictType::getCreateTime);
        Page<SystemDictType> systemDictTypePage = new Page<>(systemDictTypeDto.getCurrentPage(), systemDictTypeDto.getPageSize());
        List<SystemDictType> systemDictTypes = systemDictTypeMapper.selectList(systemDictTypePage, lambdaQueryWrapper);
        //添加转换成树形结构返回

        List<SystemDictTypeVo> list = BeanUtils.toBeanList(systemDictTypes, SystemDictTypeVo.class);
        log.info("字典类型列表{}"+systemDictTypes);
        return new Pages(list,systemDictTypePage.getTotal());
    }


//    public SystemDictTreeVo coverttMenuInfoDTO(SystemDictTreeVo menu, List<SystemDictTreeVo> menuList) {
//        SystemDictTreeVo node = new SystemDictTreeVo();
//        BeanUtils.copyProperties(menu, node); // 复制menu的属性到新创建的node对象
//
//        // 筛选出当前菜单的子菜单，并递归地将它们转换为树状结构
//        List<SystemDictTreeVo> children = menuList.stream()
//                .filter(subMenu -> (menu.getId().equals(subMenu.getPid())))
//                .map(subPermission -> coverttMenuInfoDTO(subPermission, menuList))
//                .collect(Collectors.toList());
//        node.setChildren(children); // 将子菜单列表设置到node对象中
//        return node; // 返回转换后的菜单节点
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystemDictType(String id) {
        String[] split = id.split(",");
        for (String str : split) {
            //校验是否存在类型
            checkDictTypeExist(str);
            //删除子的数据
            delDictData(str);
            //删除字典类型
            systemDictTypeMapper.deleteById(str);
        }

    }



    @Override
    public void addSystemDictType(SystemDictTypeDto systemDictTypeDto) {
        //校验是否重复
        checkDuplicateData(systemDictTypeDto);
        SystemDictType dictType = BeanUtils.toBean(systemDictTypeDto, SystemDictType.class);
        LocalDateTime EMPTY = buildTime(1970, 1, 1);
        dictType.setDeletedTime(Timestamp.valueOf(EMPTY));
        dictType.setId(IdUtils.create());
        dictType.setDeleted(IsDeleteEnum.NORMAL.getType());
        systemDictTypeMapper.insert(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemDictType(SystemDictTypeDto systemDictTypeDto) {
        //校验是否存在类型
        checkDictTypeExist(systemDictTypeDto.getId());
        //先查询原有的数据信息
        SystemDictType systemDictType = systemDictTypeMapper.selectById(systemDictTypeDto.getId());
        //获取修改前的字典类型
        String type = systemDictType.getType();
        //相同表示 同一条数据
        if (!type.equals(systemDictTypeDto.getType())){
            //不相同 则查询是否存在
            //查询字典类型对应的所有数据
            LambdaQueryWrapper<SystemDictType>lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType());
            lambdaQueryWrapper.eq(SystemDictType::getType,systemDictTypeDto.getType());
            List<SystemDictType> systemDictDataList = systemDictTypeMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemDictDataList)){
                throw new BusinessException("字典标识已存在数据");
            }
        }

        checkDictTypeStatus(systemDictTypeDto);
        SystemDictType dictType = BeanUtils.toBean(systemDictTypeDto, SystemDictType.class);
        systemDictTypeMapper.updateById(dictType);
    }
    private void checkDictTypeStatus(SystemDictTypeDto systemDictTypeDto) {
        if (Objects.nonNull(systemDictTypeDto.getStatus())) {
            if (systemDictTypeDto.getStatus()==CommonStatusEnum.DISABLE.getStatus()) {
                SystemDictType systemDictType = systemDictTypeMapper.selectById(systemDictTypeDto.getId());
                LambdaQueryWrapper<SystemDictData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SystemDictData::getDictType, systemDictType.getType())
                        .eq(SystemDictData::getDeleted, IsDeleteEnum.NORMAL.getType());
                SystemDictData data = new SystemDictData();
                data.setDictType(systemDictType.getType());
                data.setStatus(systemDictTypeDto.getStatus());
                systemDictDataMapper.update(data, queryWrapper);
            }
        }
    }

    @Override
    public SystemDictTypeVo getSystemDictTypeDetails(String id) {
        SystemDictType systemDictType = systemDictTypeMapper.selectById(id);
        SystemDictTypeVo systemDictTypeVo = BeanUtils.toBean(systemDictType, SystemDictTypeVo.class);
        return systemDictTypeVo;
    }

    @Override
    public List<SystemDictTypeVo> getDictTypeList() {
        LambdaQueryWrapper<SystemDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemDictType> dictTypeList = systemDictTypeMapper.selectList(lambdaQueryWrapper);
        List<SystemDictTypeVo> systemDictTypes = BeanUtils.toBeanList(dictTypeList, SystemDictTypeVo.class);
        return systemDictTypes;
    }

    @Override
    public Map<String,Object> getAllTypeDataList() {
        HashMap<String, Object> map = new HashMap<>();
        //获取所有的type类型
        List<SystemDictType> systemDictTypeList = systemDictTypeMapper.selectList(new LambdaQueryWrapper<SystemDictType>().eq(SystemDictType::getDeleted, IsDeleteEnum.NORMAL.getType()));
        List<SystemDictTypeDataVo> dictDataVos = BeanUtils.toBeanList(systemDictTypeList, SystemDictTypeDataVo.class);
        //获取所有的数据类型
        List<SystemDictData> systemDictData1 = systemDictDataMapper.selectList(null);
        List<SystemDictDataVo> systemDictDataVos1 = BeanUtils.toBeanList(systemDictData1, SystemDictDataVo.class);
        //根据 类型的type 查询数据
        List<SystemDictTypeDataVo> collect = dictDataVos.stream().map(dictType -> {
            ArrayList<SystemDictDataVo> result = new ArrayList<>();
            LambdaQueryWrapper<SystemDictData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemDictData::getDeleted, IsDeleteEnum.NORMAL.getType())
                    .eq(SystemDictData::getDictType, dictType.getType())
                    .orderByAsc(SystemDictData::getSort);
            List<SystemDictData> systemDictData = systemDictDataMapper.selectList(queryWrapper);
            List<SystemDictDataVo> systemDictDataVos = BeanUtils.toBeanList(systemDictData, SystemDictDataVo.class);
            systemDictDataVos.stream().filter(data->data.getPid()==null).forEach(data->{
                result.add(coverttMenuInfoDTO(data, systemDictDataVos1));
            });
            dictType.setDictDataList(BeanUtils.toBeanList(systemDictData, DictDataVo.class));
            map.put(dictType.getType(),result);
            return dictType;
        }).collect(Collectors.toList());
        return map;
    }

    public SystemDictDataVo coverttMenuInfoDTO(SystemDictDataVo menu, List<SystemDictDataVo> menuList) {
        SystemDictDataVo node = new SystemDictDataVo();
        BeanUtils.copyProperties(menu, node); // 复制menu的属性到新创建的node对象

        // 筛选出当前菜单的子菜单，并递归地将它们转换为树状结构
        List<SystemDictDataVo> children = menuList.stream()
                .filter(subMenu -> (menu.getId().equals(subMenu.getPid())))
                .map(subPermission -> coverttMenuInfoDTO(subPermission, menuList))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(children)){
            node.setChildren(null); // 将子菜单列表设置到node对象中
        }else {
            node.setChildren(children); // 将子菜单列表设置到node对象中
        }
        return node; // 返回转换后的菜单节点
    }
    /**
     * 校验是否存在
     * @param id
     */
    public void checkDictTypeExist(String id) {
        LambdaQueryWrapper<SystemDictType> queryWrapper = new LambdaQueryWrapper<SystemDictType>()
                .eq(SystemDictType::getDeleted, IsDeleteEnum.NORMAL.getType())
                .eq(SystemDictType::getId, id);
        SystemDictType systemDictType = systemDictTypeMapper.selectOne(queryWrapper);
        if (Objects.isNull(systemDictType)){
            throw new BusinessException("当前字典类型不存在");
        }
    }

    /**
     * 校验是否重复
     * @param systemDictTypeDto
     */
    public void checkDuplicateData(SystemDictTypeDto systemDictTypeDto) {
        //字典名称
//        if (!StringUtils.isEmpty(systemDictTypeDto.getName())){
//            LambdaQueryWrapper<SystemDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(SystemDictType::getName,systemDictTypeDto.getName());
//            lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType());
//            List<SystemDictType> systemDictTypes = systemDictTypeMapper.selectList(lambdaQueryWrapper);
//            if (!CollectionUtils.isEmpty(systemDictTypes)){
//                throw new BusinessException("字典名称重复");
//            }
//        }

        //字典类型
        if (!StringUtils.isEmpty(systemDictTypeDto.getType())){
            LambdaQueryWrapper<SystemDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemDictType::getType,systemDictTypeDto.getType());
            lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemDictType> systemDictTypes = systemDictTypeMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemDictTypes)){
                throw new BusinessException("字典标识重复");
            }
        }
    }


    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

    /**
     * 校验是否有字典数据
     * @param id
     */
    public void delDictData(String id) {
        LambdaQueryWrapper<SystemDictType> queryWrapper = new LambdaQueryWrapper<SystemDictType>()
                .eq(SystemDictType::getDeleted, IsDeleteEnum.NORMAL.getType())
                .eq(SystemDictType::getId, id);
        SystemDictType systemDictType = systemDictTypeMapper.selectOne(queryWrapper);
        String dictTypeType = systemDictType.getType();
        LambdaQueryWrapper<SystemDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemDictData::getDictType,dictTypeType);
        systemDictDataMapper.delete(lambdaQueryWrapper);

    }
}
