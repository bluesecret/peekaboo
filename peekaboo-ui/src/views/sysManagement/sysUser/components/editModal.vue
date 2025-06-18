<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-29 17:08:05
 * @LastEditTime: 2024-04-16 18:05:44
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
      :label-col-props="{ span: 6 }"
      :wrapper-col-props="{ span: 18 }"
      label-align="right"
      :rules="modalFormRules"
    >
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="avatar"
            label="头像"
            show-colon
            :label-col-props="{ span: 3 }"
            :wrapper-col-props="{ span: 18 }"
          >
            <a-upload
              action="/"
              :fileList="avatarFile ? [avatarFile] : []"
              :show-file-list="false"
              accept=".jpg,jpeg,.gif,.png,.svg,.bpm"
              :auto-upload="false"
              image-preview
              @change="onChange"
              @progress="onProgress"
              @before-upload="beforeUpload"
            >
              <template #upload-button>
                <div
                  :class="`arco-upload-list-item${
                    avatarFile && avatarFile.status === 'error'
                      ? ' arco-upload-list-item-error'
                      : ''
                  } m-0`"
                >
                  <div
                    class="arco-upload-list-picture custom-upload-avatar rounded-full"
                    v-if="avatarFile && avatarFile.url"
                  >
                    <img :src="avatarFile.url" />
                    <div class="arco-upload-list-picture-mask">
                      <IconEdit />
                    </div>
                    <a-progress
                      v-if="
                        avatarFile.status === 'uploading' &&
                        avatarFile.percent < 100
                      "
                      :percent="avatarFile.percent"
                      type="circle"
                      size="mini"
                      :style="{
                        position: 'absolute',
                        left: '50%',
                        top: '50%',
                        transform: 'translateX(-50%) translateY(-50%)',
                      }"
                    />
                  </div>
                  <div class="arco-upload-picture-card rounded-full" v-else>
                    <div class="arco-upload-picture-card-text">
                      <IconPlus />
                      <div style="margin-top: 10px; font-weight: 600">
                        上传头像
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </a-upload>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="username" label="用户名" show-colon>
            <a-input
              v-model.trim="modalForm.username"
              placeholder="请输入用户名"
              allow-clear
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item field="nickname" label="昵称" show-colon>
            <a-input
              v-model.trim="modalForm.nickname"
              placeholder="请输入昵称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="deptIds" label="部门" show-colon>
            <a-tree-select
              :data="depts.slice(1)"
              v-model="modalForm.deptIds"
              :fieldNames="{
                key: 'value',
                title: 'label',
                children: 'children',
              }"
              :filter-tree-node="filterTreeNode"
              :tree-check-strictly="true"
              placeholder="请选择部门"
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
        <a-col :span="12">
          <a-form-item field="roleIds" label="角色" show-colon>
            <a-select
              v-model="modalForm.roleIds"
              placeholder="请选择角色"
              multiple
              scrollbar
            >
              <a-option
                v-for="item in roleList"
                :key="item.id"
                :value="item.id"
                >{{ item.name }}</a-option
              >
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="postIds" label="岗位" show-colon>
            <a-select
              v-model="modalForm.postIds"
              placeholder="请选择岗位"
              multiple
              scrollbar
            >
              <a-option
                v-for="item in postList"
                :key="item.id"
                :value="item.id"
                >{{ item.name }}</a-option
              >
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item field="mobile" label="手机号" show-colon>
            <a-input
              v-model.trim="modalForm.mobile"
              placeholder="请输入手机号"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item field="email" label="邮箱" show-colon>
            <a-input
              v-model.trim="modalForm.email"
              placeholder="请输入邮箱"
              allow-clear
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item field="sex" label="性别" show-colon>
            <a-radio-group v-model="modalForm.sex">
              <a-radio :value="0">男</a-radio>
              <a-radio :value="1">女</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="status"
            label="状态"
            show-colon
            :label-col-props="{ span: 3 }"
            :wrapper-col-props="{ span: 21 }"
          >
            <a-radio-group v-model="modalForm.status">
              <a-radio :value="0">正常</a-radio>
              <a-radio :value="1">禁用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="remark"
            label=" 备注"
            show-colon
            :label-col-props="{ span: 3 }"
            :wrapper-col-props="{ span: 21 }"
          >
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
import { ref, watch, nextTick } from "vue";
import user from "@/api/system/user";
import { Message } from "@arco-design/web-vue";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      username: "",
      nickname: "",
      deptIds: undefined,
      mobile: "",
      status: 0,
      roleIds: undefined,
      postIds: undefined,
      email: "",
      sex: 0,
      remark: "",
    },
  },
  depts: {
    type: Array,
    default: [],
  },
  roleList: {
    type: Array,
    default: [],
  },
  postList: {
    type: Array,
    default: [],
  },
});
const emit = defineEmits();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  username: "",
  nickname: "",
  deptIds: undefined,
  mobile: "",
  status: 0,
  roleIds: undefined,
  postIds: undefined,
  email: "",
  sex: 0,
  remark: "",
});
const modalFormRules = {
  username: [
    {
      required: true,
      validator: (v, b) => {
        if (v) {
          if (/[\u4e00-\u9fa5]/g.test(v)) {
            b("用户名不能存在中文");
          } else {
            b();
          }
        } else {
          b("用户名不能为空");
        }
      },
    },
  ],
  nickname: [
    {
      required: true,
      message: "昵称不能为空",
    },
  ],
  deptIds: [
    {
      required: true,
      message: "部门不能为空",
    },
  ],
  roleIds: [
    {
      required: true,
      message: "角色不能为空",
    },
  ],
  mobile: [
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
  email: [
    {
      required: false,
      validator: (v, b) => {
        v &&
        !/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(v)
          ? b("邮箱格式不正确")
          : b();
      },
      trigger: "blur",
    },
  ],
};
const avatarFile = ref();

const openModal = (type) => {
  modalType.value = type;
  setFormVisible.value = true;

  nextTick(() => {
    if (type == 2) {
      avatarFile.value = {
        url: props.editForm.avatar,
        percent: 100,
        status: "success",
      };
    } else {
      avatarFile.value = {};
    }
  });
};
defineExpose({ openModal });
const onChange = (_, currentFile) => {
  avatarFile.value = {
    ...currentFile,
  };
};
const onProgress = (currentFile) => {
  avatarFile.value = currentFile;
};
const beforeUpload = (file) => {
  if (file.size > 1024 * 1024 * 2) {
    Message.error("图片大小不能超过2M");
    return false;
  } else {
    return true;
  }
};
const filterTreeNode = (searchValue, nodeData) => {
  return nodeData.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};
const handleCancel = () => {
  setFormVisible.value = false;
  modalFormRef.value.resetFields();
};
const handleBeforeOk = async (done) => {
  // console.log(avatarFile.value.file);
  const validate = await modalFormRef.value.validate();
  if (validate) {
    return done(false);
  }
  const formData = new FormData();
  if (avatarFile.value && avatarFile.value.file) {
    formData.append("file", avatarFile.value.file);
  }
  for (let key in modalForm.value) {
    if (modalForm.value.hasOwnProperty(key) && key !== "avatar") {
      let value = modalForm.value[key];
      if (value === null || value === undefined) value = "";
      formData.append(key, value);
    }
  }
  try {
    if (modalType.value === 1) {
      const resData = await user.save(formData);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await user.update(formData);
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
export default { name: "sysManagement:sysUser:editModal" };
</script>

<style lang="less" scoped></style>
