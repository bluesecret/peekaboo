package io.wangk.peekaboo.server.admin.log.record;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtils;
import cn.ruixi.azure.rainbow.boot.common.util.RequestUtil;
import cn.ruixi.azure.rainbow.boot.common.util.SpringContextUtils;
import cn.ruixi.azure.rainbow.boot.common.util.UUIDUtils;
import cn.ruixi.azure.rainbow.boot.module.system.api.dto.bo.SystemUserDto;
import cn.ruixi.azure.rainbow.boot.module.system.repository.entity.SystemLog;
import cn.ruixi.azure.rainbow.boot.module.system.repository.mapper.SystemUsersMapper;
import cn.ruixi.azure.rainbow.boot.module.system.repository.service.SystemLogService;
import cn.ruixi.azure.rainbow.component.record.RecordConsumer;
import cn.ruixi.azure.rainbow.component.record.RecordDefinition;
import cn.ruixi.azure.rainbow.component.record.consumer.GlobalRecordConsumer;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author bijie
 * @since 2024/4/8
 */
@Component
public class LogRecordConsumer implements GlobalRecordConsumer {


    @Autowired
    SystemLogService systemLogService;

    @Autowired
    private SystemUsersMapper systemUsersMapper;
    @Override
    public void accept(RecordDefinition recordDefinition) {
        SystemLog log = new SystemLog();
        //请求的方法名
        String methodName = recordDefinition.getMethodName();
        long startTime = recordDefinition.getStartTime();
        long stopTime = recordDefinition.getStopTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = stopTime - startTime;
        //请求的参数
        List<Object> argList = new ArrayList<>();
        Object[] args = recordDefinition.getArgs();
        for (Object arg : args) {
            if (arg instanceof ServletResponse || arg instanceof ServletRequest || arg instanceof InputStreamSource) {
                continue;
            }
            argList.add(arg);
        }
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        SystemUserDto selectByUsername = systemUsersMapper.selectByUsername(currentUsername);
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        log.setId(IdUtils.create());
        log.setUsername(currentUsername);
        log.setNickName(selectByUsername.getNickname());
        log.setCostTime(new BigDecimal(time));
        log.setIpAddr(RequestUtil.getRemoteIp(request));
        log.setMethod(recordDefinition.getClassName() + "." + methodName + "()");
        log.setRequestUrl(request.getRequestURI());
        log.setRequestType(request.getMethod());
        log.setRequestParam(JSON.toJSONString(argList));
        log.setCreateUser(currentUsername);
        log.setLogContent(recordDefinition.getDescription());
        if (Objects.nonNull(recordDefinition.getResult())) {
            log.setResult(JSON.toJSONString(recordDefinition.getResult()));
        }
        log.setLogType(recordDefinition.getLabel());
        log.setCreateTime(DateUtils.getDateTimeStr());
        log.setStartTime(formatter.format(new Date(startTime)));
        log.setStopTime(formatter.format(new Date(stopTime)));
        systemLogService.save(log);
    }
}
