<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-01 11:37:01
 * @LastEditTime: 2024-04-10 09:28:55
 * @LastEditors: wangyuanjie
-->
<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-29 17:08:05
 * @LastEditTime: 2024-04-01 09:48:55
 * @LastEditors: wangyuanjie
-->
<template>
  <a-modal
    v-model:visible="setFormVisible"
    @cancel="handleCancel"
    :on-before-ok="handleBeforeOk"
    width="700px"
  >
    <template #title>
      {{ modalType == 1 ? "新增" : "编辑" }}
    </template>
    <a-form
      ref="modalFormRef"
      :model="modalForm"
      :label-col-props="{ span: 4 }"
      :wrapper-col-props="{ span: 20 }"
      label-align="right"
      :rules="modalFormRules"
    >
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="parentId" label="上级部门" show-colon>
            <a-tree-select
              :data="deptList"
              v-model="modalForm.parentId"
              :fieldNames="{
                key: 'value',
                title: 'label',
                children: 'children',
              }"
              :filter-tree-node="filterTreeNode"
              :tree-check-strictly="true"
              placeholder="请选择部门"
              allow-clear
              allow-search
              :treeProps="{
                virtualListProps: {
                  height: 200,
                },
              }"
            ></a-tree-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="name" label="部门名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入部门名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="code" label="部门代码" show-colon>
            <a-input
              v-model.trim="modalForm.code"
              placeholder="请输入部门代码"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="leaderUserId" label="负责人" show-colon>
            <a-select
              v-model="modalForm.leaderUserId"
              placeholder="请选择负责人"
              allow-clear
              allow-search
              scrollbar
              :virtual-list-props="{ height: 200, threshold: 40 }"
              :options="userList"
              :field-names="{ value: 'id', label: 'nickname' }"
            >
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="phone" label="手机号" show-colon>
            <a-input
              v-model.trim="modalForm.phone"
              placeholder="请输入手机号"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="sort" label="排序" show-colon>
            <a-input-number
              v-model.trim="modalForm.sort"
              placeholder="请输入排序"
              :min="0"
              :max="1000"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="status" label="状态" show-colon>
            <a-radio-group v-model="modalForm.status">
              <a-radio :value="0">正常</a-radio>
              <a-radio :value="1">禁用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="remark" label=" 备注" show-colon>
            <a-textarea
              v-model="modalForm.remark"
              placeholder="请输入备注"
              :max-length="{ length: 250, errorOnly: false }"
              show-word-limit
              allow-clear
              :auto-size="{
                minRows: 2,
                maxRows: 5,
              }"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, watch } from "vue";
import dept from "@/api/system/dept";
import { Message } from "@arco-design/web-vue";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      parentId: undefined,
      name: "",
      code: "",
      phone: "",
      status: 0,
      leaderUserId: undefined,
      sort: 1,
      remark: "",
    },
  },
  depts: {
    type: Array,
    default: [],
  },
  userList: {
    type: Array,
    default: [],
  },
});
const emit = defineEmits();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  parentId: undefined,
  name: "",
  code: "",
  phone: "",
  status: 0,
  leaderUserId: undefined,
  sort: 1,
  remark: "",
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "部门名称不能为空",
    },
  ],
  code: [
    {
      required: true,
      message: "部门代码不能为空",
    },
  ],
  phone: [
    {
      required: false,
      trigger: "blur",
      validator: (v, b) => {
        v &&
        !/^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/.test(
          v
        )
          ? b("手机号格式不正确")
          : b();
      },
    },
  ],
};
const deptList = ref([]);

const openModal = (type) => {
  modalType.value = type;
  deptList.value = props.depts;
  deepToDisabled(deptList.value, modalForm.value.id);
  setFormVisible.value = true;
};
defineExpose({ openModal });
const filterTreeNode = (searchValue, nodeData) => {
  return nodeData.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};

const deepToDisabled = (data, id) => {
  data.forEach((item) => {
    if (item.id == id) {
      item.disabled = true;
    } else {
      item.disabled = false;
    }
    if (item.children) {
      deepToDisabled(item.children, id);
    }
  });
};
const handleCancel = () => {
  modalFormRef.value.resetFields();
  setFormVisible.value = false;
};
const handleBeforeOk = async (done) => {
  // console.log(avatarFile.value.file);
  const validate = await modalFormRef.value.validate();
  if (validate) {
    return done(false);
  }
  console.log(modalForm.value);
  const data = modalForm.value;
  try {
    if (modalType.value === 1) {
      const resData = await dept.save(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      if (!data.parentId) {
        data.parentId = "0";
      }
      const resData = await dept.update(data);
      if (resData.code == 0) {
        Message.success("修改成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    }
  } catch {
    done(false);
  } finally {
    emit("refresh");
  }
};
watch(
  () => props.editForm,
  (val) => {
    modalForm.value = val;
  },
  {
    deep: true,
    immediate: true,
  }
);
</script>

<script>
export default { name: "sysManagement:sysDept:editModal" };
</script>

<style lang="less" scoped></style>
