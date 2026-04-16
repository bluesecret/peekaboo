package io.wangk.peekaboo.common.api;

import java.util.Map;

public interface DictApi {

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param value 字典值
     * @return 字典标签
     */
    String getDictLabel(String dictType, String value);

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param label 字典标签
     * @return 字典值
     */
    String getDictValue(String dictType, String label);

    /**
     * 获取字典下所有的字典值与标签
     *
     * @param dictType 字典类型
     * @return dictValue为key，dictLabel为值组成的Map
     */
    Map<String, String> listAllDictData(String dictType);
}
