package io.wangk.peekaboo.common.api;

/**
 * 通用 用户服务
 *
 * @author Lion Li
 */
public interface UserApi {

    /**
     * 通过用户ID查询用户账户
     *
     * @param userId 用户ID
     * @return 用户账户
     */
    String getUsername(Long userId);
}
