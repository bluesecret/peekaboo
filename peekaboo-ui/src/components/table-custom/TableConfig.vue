<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-28 11:30:32
 * @LastEditTime: 2024-03-28 11:30:33
 * @LastEditors: wangyuanjie
-->
<template>
  <a-space align="center">
    <a-tooltip
      content="开启/关闭表格边框"
      class="item"
      trigger="hover"
      placement="top"
    >
      <a-button
        shape="circle"
        size="small"
        :status="border ? 'success' : 'normal'"
        @click="updateBorder"
      >
        B
      </a-button>
    </a-tooltip>
    <a-tooltip
      content="开启/关闭斑马纹"
      class="item"
      trigger="hover"
      placement="top"
    >
      <a-button
        shape="circle"
        size="small"
        :status="striped ? 'success' : 'normal'"
        @click="updateStriped"
      >
        S
      </a-button>
    </a-tooltip>
    <a-tooltip content="刷新页面" class="item" trigger="hover" placement="top">
      <a-button shape="circle" size="small" @click="doRefresh">
        <template #icon>
          <icon-refresh />
        </template>
      </a-button>
    </a-tooltip>
  </a-space>
</template>

<script>
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "TableConfig",
  emits: ["refresh", "update-border", "update-striped"],
  setup(props, { emit }) {
    function doRefresh() {
      emit("refresh");
    }
    const border = ref(false);
    const striped = ref(false);
    function updateBorder() {
      border.value = !border.value;
      emit("update-border", border.value);
    }
    function updateStriped() {
      striped.value = !striped.value;
      emit("update-striped", striped.value);
    }
    return {
      border,
      striped,
      doRefresh,
      updateBorder,
      updateStriped,
    };
  },
});
</script>
