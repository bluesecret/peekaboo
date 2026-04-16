package io.wangk.peekaboo.common.core.specific;

import io.wangk.peekaboo.common.core.KeyValue;
import lombok.Data;

@Data
public class LabelKeyValue extends KeyValue<String, String> {

    public LabelKeyValue(String key, String value) {
        super(key, value);
    }

}
