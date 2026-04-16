package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemMenuDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemMenuView;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemMenuVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemMenu;

import java.util.List;

public interface SystemMenuService extends IService<SystemMenu> {

    List<SystemMenuVo> getSystemMenuList(String name, String status, String visible);

    void deleteSystemMenu(String id);

    SystemMenuView getSystemDetails(String id);

    void updateSystemMenu(SystemMenuDto systemMenuDto);

    void addSystemMenu(SystemMenuDto systemMenuDto);

    List<SystemMenuView> getMenuList();


    List<SystemMenuVo> getMenuListTree();

    List<SystemMenuVo> getSystemMenuTreeStatusList();
}
