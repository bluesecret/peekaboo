package io.wangk.peekaboo.component.satoken.core.util;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import io.wangk.peekaboo.component.satoken.core.context.LoginUser;
import io.wangk.peekaboo.component.satoken.core.context.LoginUserContextHolderStrategy;
import io.wangk.peekaboo.component.satoken.core.context.ThreadLocalLoginUserContextHolderStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUserHolder {

    public static final String TENANT_KEY = "tenantId";
    public static final String USER_KEY = "userId";
    public static final String USER_NAME_KEY = "userName";
    public static final String DEPT_KEY = "deptId";
    public static final String DEPT_NAME_KEY = "deptName";

    private static LoginUserContextHolderStrategy strategy = new ThreadLocalLoginUserContextHolderStrategy();


    public static void login(Long userId, String username, Long deptId, String deptName, String tenantId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId).setUsername(username).setDeptId(deptId).setDeptName(deptName).setTenantId(tenantId);
        login(loginUser);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        StpUtil.login(loginUser.getUserId(),
                SaLoginConfig.setExtra(TENANT_KEY, loginUser.getTenantId())
                        .setExtra(USER_KEY, loginUser.getUserId())
                        .setExtra(USER_NAME_KEY, loginUser.getUsername())
                        .setExtra(DEPT_KEY, loginUser.getDeptId())
                        .setExtra(DEPT_NAME_KEY, loginUser.getDeptName())
        );
    }

    public static Long getLoginId() {
        return Convert.toLong(StpUtil.getLoginId());
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        return Convert.toLong(getExtra(USER_KEY));
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return Convert.toStr(getExtra(USER_NAME_KEY));
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        return Convert.toStr(getExtra(TENANT_KEY));
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return Convert.toLong(getExtra(DEPT_KEY));
    }

    /**
     * 获取部门名
     */
    public static String getDeptName() {
        return Convert.toStr(getExtra(DEPT_NAME_KEY));
    }


    /**
     * 获取当前 Token 的扩展信息
     *
     * @param key 键值
     * @return 对应的扩展数据
     */
    private static Object getExtra(String key) {
        try {
            return StpUtil.getExtra(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查当前用户是否已登录
     *
     * @return 结果
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    public static LoginUser getContext() {
        return strategy.getContext();
    }

}
