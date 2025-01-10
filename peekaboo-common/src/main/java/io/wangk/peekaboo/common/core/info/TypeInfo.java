package io.wangk.peekaboo.common.core.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeInfo {

    public static final TypeInfo EMPTY = new TypeInfo();
    private String code;
    private String name;
    private int sn = 0;

    public static TypeInfo of(Object code, Object name) {
        TypeInfo info = new TypeInfo();
        info.setCode(code.toString());
        info.setName(name.toString());
        return info;
    }
}
