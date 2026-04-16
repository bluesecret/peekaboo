package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemDeptDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDeptTreeDetailVo;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDeptTreeVo;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDeptVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemDept;

import java.util.List;


public interface SystemDepartmentService extends IService<SystemDept> {

    void addDepartment(SystemDeptDto sysDepartmentDto);

    void deleteDepartment(String id);

    List<SystemDeptTreeVo> getSysDepartmentTreeList();

    List<SystemDeptVo> getSysDepartmentList();

    void uploadSysDepartment(SystemDeptDto sysDepartmentDto);

    void updateDeptLeaders(SystemDeptDto systemDeptDto);

    List<SystemDeptTreeDetailVo> getSysDepartmentDetailList(String name, String status);

    int updateSystemDeptStatus(SystemDeptDto systemDeptDto);

    SystemDeptDto findById(String id);

    Page getPageSysDeptTreeDetailList(String name, String status, Integer currentPage, Integer pageSize);

    List<SystemDeptTreeVo> getSysDepartmentListStatus();

}
