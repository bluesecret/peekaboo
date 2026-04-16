<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-01 17:25:24
 * @LastEditTime: 2024-04-09 09:29:47
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
          <a-form-item field="name" label="角色名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入角色名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="code" label="角色标识" show-colon>
            <a-input
              v-model.trim="modalForm.code"
              placeholder="请输入角色标识"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="dataScope" label="数据范围" show-colon>
            <a-select
              v-model="modalForm.dataScope"
              placeholder="请选择数据范围"
              allow-clear
              allow-search
              :options="returnList('data_scoped')"
            >
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.dataScope == 2">
        <a-col :span="24">
          <a-form-item field="dataScopeDeptIds" label="数据权限" show-colon>
            <a-tree-select
              :data="depts"
              v-model="modalForm.dataScopeDeptIds"
              :fieldNames="{
                key: 'value',
                title: 'label',
                children: 'children',
              }"
              :filter-tree-node="filterTreeNode"
              :tree-check-strictly="true"
              placeholder="请选择数据权限"
              allow-clear
              tree-checkable
              allow-search
              multiple
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
import role from "@/api/system/role";
import { Message } from "@arco-design/web-vue";
import useListFormat from "@/hooks/dictionaries";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      name: "",
      code: "",
      dataScope: undefined,
      status: 0,
      dataScopeDeptIds: [],
      sort: 1,
      remark: "",
    },
  },
  depts: {
    type: Array,
    default: [],
  },
});
const emit = defineEmits();

const { returnList } = useListFormat();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  name: "",
  code: "",
  dataScope: undefined,
  status: 0,
  dataScopeDeptIds: [],
  sort: 1,
  remark: "",
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "角色名称不能为空",
    },
  ],
  code: [
    {
      required: true,
      validator: (v, b) => {
        if (v) {
          if (/[\u4e00-\u9fa5]/g.test(v)) {
            b("角色标识不能存在中文");
          } else {
            b();
          }
        } else {
          b("角色标识不能为空");
        }
      },
    },
  ],
  dataScope: [
    {
      required: true,
      message: "数据范围不能为空",
    },
  ],
};

const openModal = (type) => {
  modalType.value = type;
  setFormVisible.value = true;
};
defineExpose({ openModal });
const filterTreeNode = (searchValue, nodeData) => {
  return nodeData.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
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
  const data = JSON.parse(JSON.stringify(modalForm.value));
  try {
    if (modalType.value === 1) {
      const resData = await role.save(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await role.update(data);
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
export default { name: "sysManagement:sysRole:editModal" };
</script>

<style lang="less" scoped></style>
