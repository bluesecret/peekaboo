package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.PasswordEncode;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserDto;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemUserRoleDTO;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUserVo;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemUsersView;
import io.wangk.peekaboo.server.admin.repository.entity.SystemUsers;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SystemUserService extends IService<SystemUsers> {

    void deleteSystemUserById(String id);

    void addSystemUser(MultipartFile file, SystemUserDto systemUserDto);

    void updateSystemUser(MultipartFile file,SystemUserDto systemUserDto) throws IOException;

    SystemUserVo getSystemUserDetails(String id);

    void updateUserStatus(SystemUserDto systemUserDto);

    SystemUserDto getSystemUserDetailsByUserName(String username);

    //获取用户角色列表信息
    Set<SystemUserRoleDTO> selectUserRoleList(String id);

    List<SystemUserVo> getSystemUserByDept(SystemUserDto systemUserDto);

    void restartPassword(String id);

    int updateSystemUserStatus(SystemUserDto systemUserDto);

    List<SystemUserVo> getSysUserList();

    void importUser(MultipartFile file);

    void export(HttpServletResponse response) throws IOException;

    boolean updatePassword(PasswordEncode passwordEncode);

    boolean updateAvatar(MultipartFile file);

    boolean updateUserInfo(SystemUsersView systemUsersView);
}
