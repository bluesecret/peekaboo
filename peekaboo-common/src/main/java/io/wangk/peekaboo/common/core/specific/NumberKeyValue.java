package io.wangk.peekaboo.common.core.specific;

import io.wangk.peekaboo.common.core.KeyValue;
import lombok.Data;

@Data
public class NumberKeyValue extends KeyValue<Number, String> {
    public NumberKeyValue(Number key, String value) {
        super(key, value);
    }
}
