package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemDictDataDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemDictDataVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemDictData;

import java.util.List;

public interface SystemDictDataService extends IService<SystemDictData> {
    Pages getSystemDictDataList(SystemDictDataDto systemDictDataDto);

    void addSystemDictData(SystemDictDataDto systemDictDataDto);

    void updateSystemDictData(SystemDictDataDto systemDictDataDto);

    void deleteSystemDictData(String id);

    SystemDictDataVo getSystemDictDataDetails(String id);

    List<SystemDictData> getDictDataByDictType(String dictType);

    List<SystemDictData> getAllTypeDataList();
}
