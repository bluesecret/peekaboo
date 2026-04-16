package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemDictDataDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemDictDataVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDictData;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemDictType;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemDictDataMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemDictTypeMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemDictDataService;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @Author: bijie
 * @Date: 2024-01-11 15:14
 */
@Slf4j
@Service
public class SystemDictDataServiceImpl extends ServiceImpl<SystemDictDataMapper, SystemDictData> implements SystemDictDataService {

    @Resource
    private SystemDictDataMapper systemDictDataMapper;

    @Resource
    private SystemDictTypeMapper systemDictTypeMapper;

    @Override
    public Pages getSystemDictDataList(SystemDictDataDto systemDictDataDto) {
        //获取用户列表
        LambdaQueryWrapper<SystemDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemDictData::getDeleted,IsDeleteEnum.NORMAL.getType());
        lambdaQueryWrapper.eq(StringUtils.hasText(systemDictDataDto.getDictType()),SystemDictData::getDictType,systemDictDataDto.getDictType());
        lambdaQueryWrapper.like(StringUtils.hasText(systemDictDataDto.getLabel()),SystemDictData::getLabel,systemDictDataDto.getLabel());
        lambdaQueryWrapper.eq(Objects.nonNull(systemDictDataDto.getStatus()),SystemDictData::getStatus,systemDictDataDto.getStatus());
        lambdaQueryWrapper.like(StringUtils.hasText(systemDictDataDto.getValue()),SystemDictData::getValue,systemDictDataDto.getValue());
        lambdaQueryWrapper.isNull(SystemDictData::getPid);
        lambdaQueryWrapper.orderByAsc(SystemDictData::getSort);
        Page<SystemDictData> systemDictDataPage = new Page<>(systemDictDataDto.getCurrentPage(), systemDictDataDto.getPageSize());
        //获取分页列表
        List<SystemDictData> systemDictDataList = systemDictDataMapper.selectList(systemDictDataPage, lambdaQueryWrapper);
        lambdaQueryWrapper.clear();
        lambdaQueryWrapper.eq(SystemDictData::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemDictData> allList = systemDictDataMapper.selectList(lambdaQueryWrapper);
        List<SystemDictDataVo> list1 = BeanUtils.toBeanList(allList, SystemDictDataVo.class);
        List<SystemDictDataVo> list = BeanUtils.toBeanList(systemDictDataList, SystemDictDataVo.class);
        ArrayList<SystemDictDataVo> result = new ArrayList<>();
        list.forEach(data->{
            result.add(coverttMenuInfoDTO(data, list1));
        });
        return new Pages(result,systemDictDataPage.getTotal());
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
    @Override
    public void addSystemDictData(SystemDictDataDto systemDictDataDto) {
        //校验数据
        checkDuplicateData(systemDictDataDto);
        SystemDictData systemDictData = BeanUtils.toBean(systemDictDataDto, SystemDictData.class);
        systemDictData.setId(IdUtils.create());
        systemDictData.setDeleted(IsDeleteEnum.NORMAL.getType());
        systemDictDataMapper.insert(systemDictData);

    }

    @Override
    public void updateSystemDictData(SystemDictDataDto systemDictDataDto) {
        //校验是否存在
        validateDictDataExists(systemDictDataDto.getId());
        SystemDictData systemDictData1 = systemDictDataMapper.selectById(systemDictDataDto.getId());
        if (!Objects.equals(systemDictData1.getDictType(),systemDictDataDto.getDictType())){
            checkDuplicateData(systemDictDataDto);
        }
        SystemDictData systemDictData = BeanUtils.toBean(systemDictDataDto, SystemDictData.class);
        systemDictDataMapper.updateById(systemDictData);
    }

    @Override
    public void deleteSystemDictData(String id) {
        String[] split = id.split(",");
        for (String s : split) {
            validateDictDataExists(s);
            //删除
            SystemDictData dictData = SystemDictData.builder().id(s).deleted(IsDeleteEnum.DISABLE.getType()).build();
            systemDictDataMapper.updateById(dictData);
        }
        //校验是否存在

    }

    @Override
    public SystemDictDataVo getSystemDictDataDetails(String id) {
        //校验是否存在
        validateDictDataExists(id);
        SystemDictData systemDictData = systemDictDataMapper.selectById(id);
        SystemDictDataVo systemDictDataVo = BeanUtils.toBean(systemDictData, SystemDictDataVo.class);
        return systemDictDataVo;
    }

    @Override
    public List<SystemDictData> getDictDataByDictType(String dictType) {
        LambdaQueryWrapper<SystemDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemDictData::getDictType,dictType)
        .orderByAsc(SystemDictData::getSort)
        .orderByDesc(SystemDictData::getCreateTime);
        return systemDictDataMapper.selectList(wrapper);
    }

    @Override
    public List<SystemDictData> getAllTypeDataList() {
        LambdaQueryWrapper<SystemDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemDictData::getDeleted,IsDeleteEnum.NORMAL.getType())
                .orderByAsc(SystemDictData::getSort)
        .orderByDesc(SystemDictData::getCreateTime);
        return systemDictDataMapper.selectList(queryWrapper);
    }


    /**
     * 校验数据是否重复
     * @param systemDictDataDto
     */
    public void checkDuplicateData(SystemDictDataDto systemDictDataDto) {
        //校验字典类型是否有效
        if (!StringUtils.isEmpty(systemDictDataDto.getDictType())){
            LambdaQueryWrapper<SystemDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemDictType::getType,systemDictDataDto.getDictType());
            lambdaQueryWrapper.eq(SystemDictType::getDeleted,IsDeleteEnum.NORMAL.getType());
            SystemDictType systemDictType= systemDictTypeMapper.selectOne(lambdaQueryWrapper);
            if (Objects.isNull(systemDictType)){
                throw new BusinessException("当前字典类型不存在");
            }
            //判断该字典类型是否处于开启状态
            if (!systemDictType.getStatus().equals(CommonStatusEnum.ENABLE.getStatus())){
                throw new BusinessException("当前字典类型不处于开启状态");
            }
        }
        if (!StringUtils.isEmpty(systemDictDataDto.getDictType())&&!StringUtils.isEmpty(systemDictDataDto.getValue())){
            //校验字典数据名称是否重复
            LambdaQueryWrapper<SystemDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemDictData::getDictType,systemDictDataDto.getDictType());
            lambdaQueryWrapper.eq(SystemDictData::getValue,systemDictDataDto.getValue());
            lambdaQueryWrapper.eq(SystemDictData::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemDictData> systemDictDataList = systemDictDataMapper.selectList(lambdaQueryWrapper);
            log.info("systemDictDataList"+systemDictDataList);
            if (!CollectionUtils.isEmpty(systemDictDataList)){
                throw new BusinessException("已经存在该值的字典数据");
            }
        }

    }

    /**
     *校验是否存在
     */
    public void validateDictDataExists(String id) {
        LambdaQueryWrapper<SystemDictData> lambdaQueryWrapper = new LambdaQueryWrapper<SystemDictData>()
                .eq(SystemDictData::getDeleted, IsDeleteEnum.NORMAL.getType())
                .eq(SystemDictData::getId, id);
        SystemDictData systemDictData = systemDictDataMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(systemDictData)){
            throw new BusinessException("该字典数据不存在");
        }
    }

}
