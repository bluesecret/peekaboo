<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-28 11:31:38
 * @LastEditTime: 2024-03-28 14:46:22
 * @LastEditors: wangyuanjie
-->
<template>
  <a-card
    :body-style="{ padding: 0, width: '100%' }"
    class="table-footer-container"
    :bordered="isBordered"
  >
    <div class="flex items-center" :class="[innerPosition]">
      <a-pagination
        v-model:current="pagination.page"
        v-model:pageSize="pagination.pageSize"
        :show-page-size="pagination.showSizePicker"
        :total="pagination?.pageCount"
        show-total
        @page-size-change="onPageSizeChange"
        @change="onChange"
        size="small"
      />
      <a-button
        v-if="showRefresh"
        style="margin-left: 10px"
        shape="circle"
        size="small"
        type="primary"
        @click="refresh"
      >
        <template #icon>
          <IconLoop style="font-size: 14px" />
        </template>
      </a-button>
    </div>
  </a-card>
</template>

<script>
import { computed, defineComponent, toRef } from "vue";

export default defineComponent({
  name: "TableFooter",
  props: {
    pagination: {
      type: Object,
      default: () => ({}),
      require: true,
    },
    showRefresh: {
      type: Boolean,
      default: true,
    },
    bordered: {
      type: Boolean,
      default: false,
    },
    position: {
      type: String,
      default: "center",
    },
  },
  setup(props) {
    const pagination = toRef(props, "pagination");
    const isBordered = computed(() => props.bordered);
    const innerPosition = computed(() => {
      return "justify-" + props.position;
    });
    function onChange(page) {
      pagination.value.page = page;
      pagination.value.onChange();
    }
    function onPageSizeChange(pageSize) {
      pagination.value.pageSize = pageSize;
      pagination.value.onChange();
    }
    function refresh() {
      pagination.value.onChange();
    }
    return {
      isBordered,
      innerPosition,
      onChange,
      onPageSizeChange,
      refresh,
    };
  },
});
</script>
<style lang="less" scoped>
:deep(.arco-pagination-item-active) {
  color: var(--color-white);
}
:deep(.arco-pagination-item-active:hover) {
  color: var(--color-white);
}
.table-footer-container {
  height: 45px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}
</style>
