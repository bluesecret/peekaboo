package io.wangk.peekaboo.server.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IsDeleteEnum {

    NORMAL(0,"存在"),
    DISABLE(1,"删除");

    private final Integer type;

    private final String  value;

}
