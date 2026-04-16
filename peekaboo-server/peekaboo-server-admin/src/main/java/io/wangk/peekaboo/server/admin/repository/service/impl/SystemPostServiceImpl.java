package io.wangk.peekaboo.server.admin.repository.service.impl;

import cn.ruixi.azure.rainbow.boot.common.core.Pages;
import cn.ruixi.azure.rainbow.boot.common.util.BeanCopyUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemPostDto;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.vo.SystemPostVo;
import cn.ruixi.azure.rainbow.boot.module.system.enums.CommonStatusEnum;
import cn.ruixi.azure.rainbow.boot.module.system.enums.IsDeleteEnum;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemPost;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemUserPost;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemPostMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemUserPostMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemPostService;
import cn.ruixi.azure.rainbow.component.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: bijie
 * @Date: 2024-01-10 09:01
 */
@Service
@Slf4j
public class SystemPostServiceImpl extends ServiceImpl<SystemPostMapper, SystemPost> implements SystemPostService {

    @Resource
    private SystemPostMapper systemPostMapper;

    @Resource
    private SystemUserPostMapper systemUserPostMapper;

    @Override
    public Pages getSystemPost(SystemPostDto systemPostDto) {
        //获取岗位列表
        LambdaQueryWrapper<SystemPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemPost::getDeleted,IsDeleteEnum.NORMAL.getType());
        lambdaQueryWrapper.like(StringUtils.hasText(systemPostDto.getName()), SystemPost::getName, systemPostDto.getName());
        lambdaQueryWrapper.like(StringUtils.hasText(systemPostDto.getCode()),SystemPost::getCode,systemPostDto.getCode());
        lambdaQueryWrapper.eq(Objects.nonNull(systemPostDto.getStatus()),SystemPost::getStatus,systemPostDto.getStatus());

        lambdaQueryWrapper.orderByAsc(SystemPost::getSort);
        Page<SystemPost> page = new Page<>(systemPostDto.getCurrentPage(), systemPostDto.getPageSize());
        //获取分页列表
        List<SystemPost> systemPostList = systemPostMapper.selectList(page, lambdaQueryWrapper);
        log.info("岗位列表为{}"+systemPostList);
        List<SystemPostVo> list = BeanUtils.toBeanList(systemPostList, SystemPostVo.class);
       return new Pages(list,page.getTotal());

    }

    @Override
    public void deleteSystemPost(String id) {
        String[] strings = id.split(",");
        for (String str : strings) {
            //检验岗位是否存在
            validatePostExists(str);
            //删除岗位的同时将关联关系删除
            //获取绑定列表
            List<String> userPostIds = getUserPostIds(str);
            if (!CollectionUtils.isEmpty(userPostIds)){
                systemUserPostMapper.deleteBatchIds(userPostIds);
            }
            SystemPost systemPost = SystemPost.builder().id(str).deleted(IsDeleteEnum.DISABLE.getType()).build();
            systemPostMapper.updateById(systemPost);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemPostStatus(SystemPostDto systemPostDto) {
        checkCodeAndName(systemPostDto);


        SystemPost systemPost = BeanUtils.toBean(systemPostDto, SystemPost.class);
        checkDeptStatus(systemPostDto);
        systemPostMapper.updateById(systemPost);
    }
    private void checkDeptStatus(SystemPostDto systemPostDto) {
        LambdaQueryWrapper<SystemUserPost> queryWrapper = new LambdaQueryWrapper<SystemUserPost>()
                .eq(SystemUserPost::getPostId, systemPostDto.getId());
        SystemUserPost systemUserPost = new SystemUserPost();
        //判断如果状态==1则修改关联表关系
        if (systemPostDto.getStatus().equals(CommonStatusEnum.DISABLE.getStatus()) ){
            systemUserPost.setStatus("1");
            systemUserPostMapper.update(systemUserPost, queryWrapper);
        }else {
            systemUserPost.setStatus("0");
            systemUserPostMapper.update(systemUserPost, queryWrapper);

        }
    }


    @Override
    public void addSystemPost(SystemPostDto systemPostDto) {
        SystemPost systemPost = BeanUtils.toBean(systemPostDto, SystemPost.class);
        //判断岗位编码和岗位名称是否重复
        checkCodeAndName(systemPostDto);
        systemPost.setId(IdUtils.create());
        systemPost.setDeleted(IsDeleteEnum.NORMAL.getType());
        systemPostMapper.insert(systemPost);
    }

    @Override
    public void updateSystemPost(SystemPostDto systemPostDto) {
        //检验岗位是否存在
        validatePostExists(systemPostDto.getId());
        SystemPost byId = systemPostMapper.selectById(systemPostDto.getId());
        if (!Objects.equals(byId.getCode(),systemPostDto.getCode())){
            LambdaQueryWrapper<SystemPost> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SystemPost::getCode,systemPostDto.getCode())
                    .eq(SystemPost::getDeleted,IsDeleteEnum.NORMAL.getType());
            if (systemPostMapper.selectCount(wrapper)>0){
                throw new BusinessException("岗位标识重复");
            }
        }
        SystemPost systemPost = BeanUtils.toBean(systemPostDto, SystemPost.class);
        systemPostMapper.updateById(systemPost);
    }

    @Override
    public SystemPostVo getSystemPostDetails(String id) {
        //检验岗位是否存在
        validatePostExists(id);
        SystemPost post = systemPostMapper.selectById(id);
        SystemPostVo systemPostVo = BeanUtils.toBean(post, SystemPostVo.class);
        return systemPostVo;
    }

    @Override
    public List<SystemPostVo> getPostList() {
        LambdaQueryWrapper<SystemPost> lambdaQueryWrapper = new LambdaQueryWrapper<SystemPost>()
                .eq(SystemPost::getDeleted, IsDeleteEnum.NORMAL.getType())
                .eq(SystemPost::getStatus, CommonStatusEnum.ENABLE.getStatus());
        List<SystemPost> systemPostList = systemPostMapper.selectList(lambdaQueryWrapper);
        List<SystemPostVo> systemPostVoList = BeanUtils.toBeanList(systemPostList, SystemPostVo.class);
        return systemPostVoList;
    }

    /**
     *  判断岗位编码和岗位名称是否重复
     * @param systemPostDto
     */
    private void checkCodeAndName(SystemPostDto systemPostDto) {
        //岗位编码
        if (!StringUtils.isEmpty(systemPostDto.getCode())){
            LambdaQueryWrapper<SystemPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SystemPost::getCode,systemPostDto.getCode());
            lambdaQueryWrapper.eq(SystemPost::getDeleted,IsDeleteEnum.NORMAL.getType());
            List<SystemPost> systemPostList = systemPostMapper.selectList(lambdaQueryWrapper);
            if (!CollectionUtils.isEmpty(systemPostList)){
                throw new BusinessException("岗位标识已存在");
            }
        }
//        //岗位名称
//        if (!StringUtils.isEmpty(systemPostDto.getName())){
//            LambdaQueryWrapper<SystemPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(SystemPost::getName,systemPostDto.getName());
//            lambdaQueryWrapper.eq(SystemPost::getDeleted,IsDeleteEnum.NORMAL.getType());
//            List<SystemPost> systemPostList = systemPostMapper.selectList(lambdaQueryWrapper);
//            if (!CollectionUtils.isEmpty(systemPostList)){
//                throw new BusinessException("岗位名称已存在");
//            }
//        }
    }

    /**
     * 获取用户岗位的关系列表
     */
    public List<String> getUserPostIds(String id){
        LambdaQueryWrapper<SystemUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemUserPost::getPostId,id);
        lambdaQueryWrapper.eq(SystemUserPost::getDeleted,IsDeleteEnum.NORMAL.getType());
        List<SystemUserPost> systemUserPostList = systemUserPostMapper.selectList(lambdaQueryWrapper);
        List<String> postIds = systemUserPostList.stream()
                .map(SystemUserPost::getId)
                .distinct()
                .collect(Collectors.toList());
        return postIds;
    }

    /**
     * 检验岗位是否存在
     */
    public void validatePostExists(String id){
        LambdaQueryWrapper<SystemPost> lambdaQueryWrapper = new LambdaQueryWrapper<SystemPost>()
                .eq(SystemPost::getId,id)
                .eq(SystemPost::getDeleted, IsDeleteEnum.NORMAL.getType());
        SystemPost post = systemPostMapper.selectOne(lambdaQueryWrapper);
        if(Objects.isNull(post)){
            throw new BusinessException("岗位不存在");
        }
    }


}
