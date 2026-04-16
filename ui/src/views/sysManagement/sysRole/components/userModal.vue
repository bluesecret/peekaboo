<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-02 14:51:44
 * @LastEditTime: 2024-04-02 17:21:26
 * @LastEditors: wangyuanjie
-->
<template>
  <a-modal
    v-model:visible="setUserVisible"
    @cancel="handleCancel"
    :on-before-ok="handleBeforeOk"
    width="700px"
    title="绑定用户"
  >
    <a-descriptions :data="desData" bordered />
    <div class="flex justify-center w-full transferBody mt-2">
      <a-transfer
        :data="sourceData"
        v-model="selectedData"
        show-search
        show-select-all
        :title="['未绑定用户', '已绑定用户']"
      >
        <template #item="{ label }">
          <icon-user />
          {{ label }}
        </template>
      </a-transfer>
    </div>
  </a-modal>
</template>

<script setup>
import { ref, watch } from "vue";
import role from "@/api/system/role";
import { Message } from "@arco-design/web-vue";
import commonApi from "@/api/common";

const props = defineProps({
  selectedKey: {
    type: Array,
    default: [],
  },
  bindUserRoleId: {
    type: Object,
    default: {},
  },
});
const emit = defineEmits();

const setUserVisible = ref(false);
const sourceData = ref([]);
const selectedData = ref([]);
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
  const res = await commonApi.getUserList();
  if (res.code == 0) {
    sourceData.value = res.data.map((item) => {
      return {
        value: item.id,
        label: item.nickname,
        disabled: !(item.status == 0),
      };
    });
    desData.value[0].value = props.bindUserRoleId.roleName;
    desData.value[1].value = props.bindUserRoleId.roleCode;
    // console.log(props.bindUserRoleId.roleName);
    setUserVisible.value = true;
  }
};
defineExpose({ openModal });

const handleCancel = () => {
  selectedData.value = [];
  setUserVisible.value = false;
};
const handleBeforeOk = async (done) => {
  const data = {
    id: props.bindUserRoleId.id,
    userList: selectedData.value,
  };
  try {
    const resData = await role.bindRoleUser(data);
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
  () => props.selectedKey,
  (val) => {
    selectedData.value = val;
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
