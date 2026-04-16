package io.wangk.peekaboo.server.admin.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.wangk.peekaboo.server.admin.api.dto.bo.SystemPostDto;
import io.wangk.peekaboo.server.admin.api.dto.vo.SystemPostVo;
import io.wangk.peekaboo.server.admin.repository.entity.SystemPost;

import java.util.List;

public interface SystemPostService extends IService<SystemPost> {
    Pages getSystemPost(SystemPostDto systemPostDto);

    void deleteSystemPost(String id);

    void updateSystemPostStatus(SystemPostDto systemPostDto);

    void addSystemPost(SystemPostDto systemPostDto);

    void updateSystemPost(SystemPostDto systemPostDto);

    SystemPostVo getSystemPostDetails(String id);

    List<SystemPostVo> getPostList();

}
