package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemDictTypeDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDictTypeVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemDictType;

import java.util.List;
import java.util.Map;

public interface SystemDictTypeService extends IService<SystemDictType> {
    Pages getSystemDictTypeList(SystemDictTypeDto systemDictTypeDto);

    void deleteSystemDictType(String id);

    void addSystemDictType(SystemDictTypeDto systemDictTypeDto);

    void updateSystemDictType(SystemDictTypeDto systemDictTypeDto);

    SystemDictTypeVo getSystemDictTypeDetails(String id);

    List<SystemDictTypeVo> getDictTypeList();

    Map<String,Object> getAllTypeDataList();


}
