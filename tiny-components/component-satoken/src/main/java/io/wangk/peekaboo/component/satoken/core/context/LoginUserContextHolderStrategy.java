package io.wangk.peekaboo.component.satoken.core.context;

import java.util.function.Supplier;

public interface LoginUserContextHolderStrategy {

    void clearContext();

    LoginUser getContext();

    default Supplier<LoginUser> getDeferredContext() {
        return this::getContext;
    }

    void setContext(LoginUser context);

    default void setDeferredContext(Supplier<LoginUser> deferredContext) {
        setContext(deferredContext.get());
    }

    LoginUser createEmptyContext();

}
