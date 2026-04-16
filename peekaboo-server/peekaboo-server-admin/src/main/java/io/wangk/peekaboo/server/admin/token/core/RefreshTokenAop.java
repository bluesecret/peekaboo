package io.wangk.peekaboo.server.admin.token.core;

import cn.ruixi.azure.rainbow.boot.module.system.token.entity.ResultMap;
import cn.ruixi.azure.rainbow.boot.security.core.util.JwtTokenManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author bijie
 * @since 2024/5/23
 */
@Aspect
@Slf4j
@Component
@Order(Integer.MAX_VALUE)
public class RefreshTokenAop {

    @Resource
    private JwtTokenManager tokenManager;

//    @Pointcut("@within(cn.ruixi.azure.rainbow.boot.module.system.token.core.RefreshToken)")
    public void pointCut() {

    }

//    @Around(value = "pointCut()")
    public Object doAroundReturningAdvice(ProceedingJoinPoint point) throws Throwable{
        Object[] args = point.getArgs();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Object result = point.proceed(args);

        ResultMap payload = new ResultMap(tokenManager).successAndRefreshToken(request).payload(result);
        return ResponseEntity.ok(payload);
    }
}
