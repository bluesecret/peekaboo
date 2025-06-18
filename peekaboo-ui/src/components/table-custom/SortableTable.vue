<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-28 11:22:29
 * @LastEditTime: 2024-04-15 11:28:26
 * @LastEditors: wangyuanjie
-->
<template>
  <a-space>
    <a-tooltip content="定时刷新" v-if="setting.refresh.show">
      <a-button
        size="small"
        :type="onSetTimer ? 'primary' : 'secondary'"
        shape="circle"
        @click="toStartTimer"
        ><icon-refresh
      /></a-button>
    </a-tooltip>
    <a-tooltip content="显隐搜索" v-if="setting.searchShowOrHide">
      <a-button size="small" shape="circle" @click="toggleSearch"
        ><icon-search
      /></a-button>
    </a-tooltip>
    <a-tooltip content="打印表格" v-if="setting.tablePrint">
      <a-button size="small" shape="circle" @click="printTable"
        ><icon-printer
      /></a-button>
    </a-tooltip>
    <a-popover
      v-if="setting.tableSort"
      :style="{ width: '200px' }"
      trigger="click"
      @popup-visible-change="onPopVisibleChange"
    >
      <template #content>
        <div
          style="border-bottom: 1px solid #f5f5f5"
          class="flex items-center justify-between"
        >
          <a-checkbox v-model="allChecked" @change="onAllChange">
            全选
          </a-checkbox>
          <a-button type="text" class="text-right" @click="onReset">
            重置
          </a-button>
        </div>
        <div class="pt-2 pb-2" id="sortColumnWrapper">
          <div
            class="column-item"
            v-for="item of innerTableProps"
            :key="item.key"
          >
            <a-checkbox
              v-model="item.checked"
              :label="item.title"
              @change="onChange"
            >
              {{ item.title }}
            </a-checkbox>
            <div class="flex-1"></div>
            <icon-menu class="handle-icon" />
          </div>
        </div>
      </template>
      <a-tooltip content="设置">
        <a-button size="small" shape="circle">
          <template #icon>
            <icon-settings />
          </template>
        </a-button>
      </a-tooltip>
    </a-popover>
  </a-space>
</template>

<script>
import {
  defineComponent,
  reactive,
  ref,
  toRef,
  nextTick,
  onUnmounted,
} from "vue";
import Sortable from "sortablejs";
import { cloneDeep, isUndefined } from "lodash";
import Print from "@/utils/print";
export default defineComponent({
  name: "SortableTable",
  props: {
    columns: {
      type: Array,
      require: false,
    },
    tableRef: {
      type: Object,
      require: false,
    },
    searchRef: {
      type: Object,
      require: false,
    },
    setting: {
      type: Object,
      require: true,
    },
  },
  emits: ["update", "refresh"],
  setup(props, { emit }) {
    const onSetTimer = ref(false);
    const tempTableProps = toRef(props, "columns");
    const originColumns = cloneDeep(
      tempTableProps.value ? tempTableProps.value : []
    );
    const tempArray =
      tempTableProps.value
        ?.filter((it) => !!it.key)
        .map((it) => {
          return {
            ...it,
            checked: true,
          };
        }) || [];
    const innerTableProps = reactive(tempArray);
    const isIndeterminate = ref(
      innerTableProps.filter((it) => it.checked).length !==
        innerTableProps.length
    );
    const allChecked = ref(innerTableProps.every((it) => it.checked));
    function onAllChange(value) {
      innerTableProps.forEach((it) => (it.checked = value));
      onUpdateValue(innerTableProps.filter((it) => it.checked));
    }
    const onChange = () => {
      const checkedItems = innerTableProps.filter((it) => it.checked);
      allChecked.value = checkedItems.length === innerTableProps.length;
      isIndeterminate.value =
        checkedItems.length > 0 &&
        checkedItems.length !== innerTableProps.length;
      onUpdateValue(checkedItems);
    };
    const onReset = () => {
      innerTableProps.length = 0;
      innerTableProps.push(...originColumns);
      innerTableProps.forEach((it) => (it.checked = true));
      allChecked.value = true;
      onUpdateValue(innerTableProps);
    };
    function onUpdateValue(columns) {
      emit(
        "update",
        columns.filter((it) => it.checked)
      );
    }
    function onPopVisibleChange(visible) {
      if (visible) {
        nextTick(() => {
          new Sortable(document.getElementById("sortColumnWrapper"), {
            handle: ".handle-icon",
            animation: 150,
            dataIdAttr: "",
            onEnd({ newIndex, oldIndex }) {
              if (isUndefined(newIndex) || isUndefined(oldIndex)) {
                return;
              }
              const originItem = innerTableProps[oldIndex];
              if (newIndex > oldIndex) {
                innerTableProps.splice(newIndex + 1, 0, originItem);
                innerTableProps.splice(oldIndex, 1);
              } else {
                innerTableProps.splice(newIndex, 0, originItem);
                innerTableProps.splice(oldIndex + 1, 1);
              }
              onUpdateValue(innerTableProps);
            },
          });
        });
      }
    }
    const printTable = () => {
      new Print(props.tableRef);
    };
    const toggleSearch = () => {
      props.searchRef.$el.style.display =
        props.searchRef.$el.style.display === "none" ? "block" : "none";
    };

    const intervalTimer = ref(null);
    const toStartTimer = () => {
      if (!onSetTimer.value) {
        onSetTimer.value = true;
        intervalTimer.value = setInterval(() => {
          emit("refresh");
        }, props.setting.refresh.time);
      } else {
        onSetTimer.value = false;
        clearInterval(intervalTimer.value);
      }
    };
    onUnmounted(() => {
      if (intervalTimer.value) {
        clearInterval(intervalTimer.value);
      }
    });
    return {
      innerTableProps,
      isIndeterminate,
      allChecked,
      intervalTimer,
      onSetTimer,
      onAllChange,
      onChange,
      onReset,
      onUpdateValue,
      onPopVisibleChange,
      printTable,
      toggleSearch,
      toStartTimer,
    };
  },
});
</script>

<style lang="less" scoped>
.column-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  .handle-icon {
    &:hover {
      cursor: move;
    }
  }
}
</style>
