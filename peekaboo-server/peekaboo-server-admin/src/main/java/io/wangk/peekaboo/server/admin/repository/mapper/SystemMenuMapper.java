package io.wangk.peekaboo.server.admin.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.wangk.peekaboo.server.admin.repository.entity.SystemMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表(SystemMenu)表数据库访问层
 * @since 2024-01-09 10:58:18
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {


}

