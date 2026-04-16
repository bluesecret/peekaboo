package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDeptTreeDetailVo;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDeptTreeVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemDept;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门表(SystemDept)表数据库访问层
 * @since 2024-01-09 09:08:20
 */
@Mapper
public interface SystemDeptMapper extends BaseMapper<SystemDept> {


    List<SystemDeptTreeVo> selectDeptTreeList();
    List<SystemDeptTreeDetailVo> selectDeptTreeDetailList(@Param("name")String name, @Param("status")String status);


    List<SystemUserDept> selectDeptUserList(String id);

    int deleteDeptUserList(String id);

    List<SystemDeptTreeVo> selectDeptTreeListStatus();

}

