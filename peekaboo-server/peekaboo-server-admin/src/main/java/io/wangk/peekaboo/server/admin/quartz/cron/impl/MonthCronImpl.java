/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.wangk.peekaboo.server.admin.quartz.cron.impl;

import cn.ruixi.azure.rainbow.boot.module.system.job.schedule.MapParam;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.cron.FunCron;
import cn.ruixi.azure.rainbow.boot.module.system.quartz.cron.StrategyFactory;
import com.alibaba.fastjson.JSONObject;
import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

@Service
public class MonthCronImpl implements FunCron {

    @Override
    public String funcDeal(String param) {
        MapParam mapParam = JSONObject.parseObject(param,MapParam.class);
        Map<String ,String> parameter = mapParam.getParameter();
        String[]  times = {"day", "hour", "minute"};
        boolean verify = verifyIsNeedParam(parameter, times);
        if(!verify){
//            throw new DataFlexServerException(Status.CREATE_ENV_ERROR);
        }
        String day = parameter.get("day");
        String hour = parameter.get("hour");
        String minute = parameter.get("minute");

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withYear(always())
                .withDoW(questionMark())
                .withMonth(always())
                .withDoM(on(Integer.parseInt(day)))
                .withHour(on(Integer.parseInt(hour)))
                .withMinute(on(Integer.parseInt(minute)))
                .withSecond(on (0))
                .instance();

        return cron.asString();
    }

    @Override
    public String getFuncName(){
        return "month";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(this.getFuncName(), this);
    }
    public static boolean verifyIsNeedParam(Map<String ,String>  parameter, String[]  times) {
        for (String time : times) {
            if (!parameter.containsKey(time)) {
                return false;
            }
            try {
                int timeValue = Integer.parseInt(parameter.get(time));
                if (timeValue > 60 || timeValue < 0) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return  true;
    }

}