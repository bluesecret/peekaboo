<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-07 15:20:10
 * @LastEditTime: 2024-04-08 18:21:01
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
          <a-form-item field="name" label="岗位名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入岗位名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="code" label="岗位标识" show-colon>
            <a-input
              v-model.trim="modalForm.code"
              placeholder="请输入岗位标识"
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
import post from "@/api/system/post";
import { Message } from "@arco-design/web-vue";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      name: "",
      code: "",
      sort: 1,
      status: 0,
      remark: "",
    },
  },
});
const emit = defineEmits();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  name: "",
  code: "",
  sort: 1,
  status: 0,
  remark: "",
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "岗位名称不能为空",
    },
  ],
  code: [
    {
      required: true,
      message: "岗位标识不能为空",
    },
  ],
};

const openModal = (type) => {
  modalType.value = type;
  setFormVisible.value = true;
};
defineExpose({ openModal });
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
      const resData = await post.save(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await post.update(data);
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
export default { name: "sysPost:editModal" };
</script>

<style lang="less" scoped></style>
