<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-02 16:28:25
 * @LastEditTime: 2024-04-08 16:16:44
 * @LastEditors: wangyuanjie
-->
<template>
  <a-modal
    v-model:visible="setMenuVisible"
    @cancel="handleCancel"
    :on-before-ok="handleBeforeOk"
    title="菜单权限"
  >
    <a-spin :loading="loading" tip="菜单加载中..." class="w-full">
      <a-descriptions :data="desData" bordered class="mt-2" />
      <div class="w-full">
        <a-space class="mt-1.5" size="large">
          <a-checkbox @change="handlerExpand">展开/折叠</a-checkbox>
          <a-checkbox @change="handlerSelect">全选/全不选</a-checkbox>
          <a-checkbox v-model="cancelLinkage" @change="handlerLinkage"
            >关闭父子级联动</a-checkbox
          >
        </a-space>
        <div class="tree-container p-2">
          <ma-tree-slider
            ref="tree"
            :data="sourceData"
            checkable
            :fieldNames="{ title: 'label', key: 'id' }"
            searchPlaceholder="过滤菜单"
            v-model:checked-keys="selectKeys"
            :check-strictly="cancelLinkage"
            :virtual-list-props="{ height: 300 }"
            @click="handlerClick"
          />
        </div>
      </div>
    </a-spin>
  </a-modal>
</template>

<script setup>
import { ref, watch } from "vue";
import role from "@/api/system/role";
import { Message } from "@arco-design/web-vue";
import commonApi from "@/api/common";
import useLoading from "@/hooks/loading";

const props = defineProps({
  bindMenuRoleId: {
    type: Object,
    default: {},
  },
});
const emit = defineEmits();

const { loading, setLoading } = useLoading(false);
const setMenuVisible = ref(false);
const sourceData = ref([]);
const selectedData = ref([]);
const selectKeys = ref([]);
const cancelLinkage = ref(false);
const tree = ref();
const desData = ref([
  {
    value: "",
    label: "角色名称",
  },
  {
    value: "",
    label: "角色标识",
  },
]);

const openModal = async () => {
  setMenuVisible.value = true;
  handlerExpand(false);
  handlerSelect(false);
  handlerLinkage(false);
  await setData();
};
defineExpose({ openModal });

const setData = async () => {
  try {
    setLoading(true);
    const resData = await commonApi.getMenuTreeList();
    sourceData.value = formatMenu(resData.data);
    const params = { id: props.bindMenuRoleId.id };
    const resData2 = await role.getBindMenuList(params);
    selectKeys.value = resData2.data.map((item) => item.id);
    selectKeys.value.length > 0 && handlerLinkage(true);
  } catch (error) {
  } finally {
    setLoading(false);
  }
};

const handleCancel = () => {
  selectedData.value = [];
  setMenuVisible.value = false;
};
const handlerExpand = (value) => {
  tree.value.maTree.expandAll(value);
};

const handlerSelect = (value) => {
  tree.value.maTree.checkAll(value);
};

const handlerLinkage = (value) => {
  cancelLinkage.value = value;
};

const handlerClick = (value) => {
  const t = tree.value.maTree;
  const nodes = t.getExpandedNodes().map((item) => item.id);
  t.expandNode(value, nodes.includes(value[0]) ? false : true);
};
const formatMenu = (data) => {
  const res = [];
  data.forEach((item) => {
    if (item.children && item.children.length > 0) {
      res.push({
        id: item.id,
        label: item.title,
        value: item.id,
        children: formatMenu(item.children),
        disabled: !(item.status == 0),
      });
    } else {
      res.push({
        id: item.id,
        label: item.title,
        value: item.id,
        disabled: !(item.status == 0),
      });
    }
  });
  return res;
};
const handleBeforeOk = async (done) => {
  console.log(selectKeys.value);
  const data = {
    id: props.bindMenuRoleId.id,
    menuList: selectKeys.value,
  };
  try {
    const resData = await role.bindRoleMenu(data);
    if (resData.code == 0) {
      Message.success("绑定成功");
      done(true);
    } else {
      done(false);
    }
  } catch {
    done(false);
  } finally {
    emit("refresh");
  }
};
watch(
  () => props.bindMenuRoleId,
  (val) => {
    desData.value[0].value = val.roleName;
    desData.value[1].value = val.roleCode;
  },
  {
    deep: true,
    immediate: true,
  }
);
</script>

<style lang="less" scoped>
.transferBody :deep(.arco-transfer-view) {
  height: 400px;
  width: 260px;
}
</style>
