package io.wangk.tiny.common.core.specific;

import io.wangk.tiny.common.core.KeyValue;
import lombok.Data;

@Data
public class NumberKeyValue extends KeyValue<Number, String> {
    public NumberKeyValue(Number key, String value) {
        super(key, value);
    }
}
