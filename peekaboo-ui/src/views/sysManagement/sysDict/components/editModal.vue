<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-03 17:47:19
 * @LastEditTime: 2024-04-08 19:29:30
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
          <a-form-item field="name" label="字典名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入字典名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="type" label="字典标识" show-colon>
            <a-input
              v-model.trim="modalForm.type"
              placeholder="请输入字典标识"
              allow-clear
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
import { dictType } from "@/api/system/dict";
import { Message } from "@arco-design/web-vue";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      name: "",
      type: "",
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
  type: "",
  status: 0,
  remark: "",
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "字典名称不能为空",
    },
  ],
  type: [
    {
      required: true,
      validator: (v, b) => {
        if (v) {
          if (/[\u4e00-\u9fa5]/g.test(v)) {
            b("字典标识不能存在中文");
          } else {
            b();
          }
        } else {
          b("字典标识不能为空");
        }
      },
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
      const resData = await dictType.save(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await dictType.update(data);
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
export default { name: "sysDict:editModal" };
</script>

<style lang="less" scoped></style>
