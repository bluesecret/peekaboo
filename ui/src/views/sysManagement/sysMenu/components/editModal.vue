<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-03 14:08:38
 * @LastEditTime: 2024-04-10 09:28:37
 * @LastEditors: wangyuanjie
-->
<template>
  <a-drawer
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
          <a-form-item field="parentId" label="上级菜单" show-colon>
            <a-tree-select
              :data="menuList"
              v-model="modalForm.parentId"
              :fieldNames="{
                key: 'id',
                value: 'id',
                title: 'name',
                children: 'children',
                icon: null,
              }"
              :filter-tree-node="filterTreeNode"
              placeholder="请选择上级菜单"
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
      <a-row>
        <a-col :span="24">
          <a-form-item field="name" label="菜单名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入菜单名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="type" label="菜单类型" show-colon>
            <a-radio-group v-model="modalForm.type">
              <a-radio :value="1">菜单</a-radio>
              <a-radio :value="2">按钮</a-radio>
              <a-radio :value="3">外链</a-radio>
              <a-radio :value="4">Iframe</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.type != 2">
        <a-col :span="24">
          <a-form-item field="icon" label="菜单图标" show-colon>
            <ma-icon-picker v-model="modalForm.icon"></ma-icon-picker>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="permission" label="菜单标识" show-colon>
            <a-input
              v-model.trim="modalForm.permission"
              placeholder="请输入菜单标识"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.type != 2">
        <a-col :span="24">
          <a-form-item field="path" label="路由地址" show-colon>
            <a-input
              v-model.trim="modalForm.path"
              placeholder="请输入路由地址"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.type == 1">
        <a-col :span="24">
          <a-form-item field="component" label="视图组件" show-colon>
            <a-input
              v-model.trim="modalForm.component"
              placeholder="请输入视图组件"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.type != 2">
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
      <a-row :gutter="16" v-if="modalForm.type != 2">
        <a-col :span="24">
          <a-form-item field="keepAlive" label="是否缓存" show-colon>
            <a-radio-group v-model="modalForm.keepAlive">
              <a-radio :value="0">是</a-radio>
              <a-radio :value="1">否</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16" v-if="modalForm.type != 2">
        <a-col :span="24">
          <a-form-item field="visible" label="是否可见" show-colon>
            <a-radio-group v-model="modalForm.visible">
              <a-radio :value="0">可见</a-radio>
              <a-radio :value="1">隐藏</a-radio>
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
  </a-drawer>
</template>

<script setup>
import { ref, watch } from "vue";
import menu from "@/api/system/menu";
import { Message } from "@arco-design/web-vue";
import MaIconPicker from "@/components/ma-icon/index.vue";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      parentId: undefined,
      name: "",
      type: 1,
      icon: "",
      status: 0,
      permission: "",
      path: "",
      component: "",
      visible: 0,
      keepAlive: 1,
      sort: 1,
      remark: "",
    },
  },
});
const emit = defineEmits();
const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const menuList = ref([]);
const modalForm = ref({
  parentId: undefined,
  name: "",
  type: 1,
  icon: "",
  status: 0,
  permission: "",
  path: "",
  component: "",
  visible: 0,
  keepAlive: 1,
  sort: 1,
  remark: "",
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "菜单名称不能为空",
    },
  ],
  permission: [
    {
      required: true,
      validator: (v, b) => {
        if (v) {
          if (/[\u4e00-\u9fa5]/g.test(v)) {
            b("菜单标识不能存在中文");
          } else {
            b();
          }
        } else {
          b("菜单标识不能为空");
        }
      },
    },
  ],
};

const openModal = async (type) => {
  const resData = await menu.getTree();
  menuList.value = resData.data;
  deepToDisabled(menuList.value, modalForm.value.id);
  modalType.value = type;
  setFormVisible.value = true;
};
defineExpose({ openModal });

const filterTreeNode = (searchValue, nodeData) => {
  return nodeData.name.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
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
  // console.log(modalForm.value);
  const data = JSON.parse(JSON.stringify(modalForm.value));
  try {
    if (modalType.value === 1) {
      const resData = await menu.save(data);
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
      const resData = await menu.update(data);
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
export default { name: "sysManagement:sysMenu:editModal" };
</script>

<style lang="less" scoped></style>
