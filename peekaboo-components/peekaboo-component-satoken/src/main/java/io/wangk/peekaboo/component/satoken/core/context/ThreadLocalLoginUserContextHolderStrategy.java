package io.wangk.peekaboo.component.satoken.core.context;

import io.wangk.peekaboo.component.satoken.core.util.LoginUserHolder;
import org.springframework.util.Assert;

import java.util.function.Supplier;

public class ThreadLocalLoginUserContextHolderStrategy implements LoginUserContextHolderStrategy {
    private static final ThreadLocal<Supplier<LoginUser>> contextHolder = new ThreadLocal<>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public LoginUser getContext() {
        return getDeferredContext().get();
    }

    @Override
    public Supplier<LoginUser> getDeferredContext() {
        Supplier<LoginUser> result = contextHolder.get();
        if (result == null) {
            LoginUser context = createEmptyContext();
            result = () -> context;
            contextHolder.set(result);
        }
        return result;
    }

    @Override
    public void setContext(LoginUser context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(() -> context);
    }

    @Override
    public void setDeferredContext(Supplier<LoginUser> deferredContext) {
        Assert.notNull(deferredContext, "Only non-null Supplier instances are permitted");
        Supplier<LoginUser> notNullDeferredContext = () -> {
            LoginUser result = deferredContext.get();
            Assert.notNull(result, "A Supplier<SecurityContext> returned null and is not allowed.");
            return result;
        };
        contextHolder.set(notNullDeferredContext);
    }

    @Override
    public LoginUser createEmptyContext() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(LoginUserHolder.getUserId());
        loginUser.setUsername(LoginUserHolder.getUsername());
        loginUser.setTenantId(LoginUserHolder.getTenantId());
        loginUser.setDeptId(LoginUserHolder.getDeptId());
        loginUser.setDeptName(LoginUserHolder.getDeptName());
        return loginUser;
    }
}
