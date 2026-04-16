<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-15 14:28:05
 * @LastEditTime: 2024-07-04 10:19:51
 * @LastEditors: wangyuanjie
-->
<template>
  <a-select
    v-if="!hasNestedOptions"
    v-model="val"
    :style="cssStyle"
    :size="size"
    :disabled="disabled"
    :allow-clear="allowClear"
    :placeholder="placeholder"
    :multiple="multiple"
    :allow-search="allowSearch"
    :bordered="bordered"
    @change="handleChange"
  >
    <template #label="{ data }">
      <a-tag
        style="margin-right: 6px; height: 22px"
        v-if="formatColor(props.dictType, data?.value)"
        :color="formatColor(props.dictType, data?.value)"
      ></a-tag
      >{{ data?.label }}
    </template>
    <a-option v-for="item in optionsList" :key="item.value" :value="item.value">
      <a-tag
        v-if="item.colorType"
        style="margin-right: 6px; height: 22px"
        :color="item.colorType"
      ></a-tag
      >{{ item.label }}
    </a-option>
  </a-select>
  <a-tree-select
    v-else
    v-model="val"
    :data="optionsList"
    :style="cssStyle"
    :size="size"
    :disabled="disabled"
    :allow-clear="allowClear"
    :placeholder="placeholder"
    :multiple="multiple"
    :allow-search="allowSearch"
    :bordered="bordered"
    :fieldNames="{
      key: 'value',
      title: 'label',
      children: 'children',
    }"
    @change="handleChange"
  >
    <template #label="{ data }">
      <a-tag
        style="margin-right: 6px; height: 22px"
        v-if="formatColor(props.dictType, data?.value)"
        :color="formatColor(props.dictType, data?.value)"
      ></a-tag
      >{{ data?.label }}
    </template>
    <template #tree-slot-icon="{ node }">
      <a-tag
        v-if="node.colorType"
        style="margin-right: 6px; height: 22px"
        :color="node.colorType"
      ></a-tag>
    </template>
  </a-tree-select>
</template>

<script setup>
import { ref, reactive, watch, computed } from "vue";
import useListFormat from "@/hooks/dictionaries";

const val = ref();

const emit = defineEmits(["update:modelValue", "change"]);
const props = defineProps({
  multiple: { type: Boolean, default: false },
  modelValue: [Number, String, Boolean, Object],
  size: { type: String, default: "medium" },
  placeholder: { type: String },
  disabled: { type: Boolean, default: false },
  allowClear: { type: Boolean, default: false },
  dictType: { type: String },
  cssStyle: { type: Object },
  bordered: { type: Boolean, default: true },
  allowSearch: {
    type: [Boolean, Object],
    default: (props) => Boolean(props.multiple),
  },
});

const { formatList, formatColor, returnList } = useListFormat();
const optionsList = ref([]);
if (props.dictType) {
  optionsList.value = returnList(props.dictType);
  // console.log(hasNestedOptions);
}

val.value = props.modelValue;

const hasNestedOptions = computed(() => {
  // 校验 optionsList 是否存在且具有 value 属性，且 value 是数组类型
  if (!optionsList || !Array.isArray(optionsList.value)) {
    console.warn("optionsList or optionsList.value is not valid");
    return false; // 如果不满足条件，直接返回 false，或者根据实际需求进行处理
  }

  try {
    // 使用 some 替代 forEach，以提高效率和表达意图
    return optionsList.value.some((item) => {
      // 检查 item 是否具有 children 属性且不为空
      return item.children && item.children.length > 0;
    });
  } catch (error) {
    console.error("Error occurred during optionsList traversal:", error);
    // 异常情况下返回 false 或者根据实际需求处理
    return false;
  }
});

const handleChange = (value) => {
  emit("change", value);
};

watch(
  () => props.modelValue,
  (vl) => {
    val.value = vl;
    // setSelectData();
  },
  { deep: true }
);

watch(
  () => props.dictType,
  (vl) => {
    optionsList.value = returnList(vl);
    // setSelectData();
  },
  { deep: true, immediate: true }
);

watch(
  () => val.value,
  (vl) => emit("update:modelValue", vl)
);
</script>

<style lang="less" scoped></style>
