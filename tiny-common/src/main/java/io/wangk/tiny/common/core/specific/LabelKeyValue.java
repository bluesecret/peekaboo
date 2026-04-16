package io.wangk.tiny.common.core.specific;

import io.wangk.tiny.common.core.KeyValue;
import lombok.Data;

@Data
public class LabelKeyValue extends KeyValue<String, String> {

    public LabelKeyValue(String key, String value) {
        super(key, value);
    }

}
