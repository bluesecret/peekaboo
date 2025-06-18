<!--
 - MineAdmin is committed to providing solutions for quickly building web applications
 - Please view the LICENSE file that was distributed with this source code,
 - For the full copyright and license information.
 - Thank you very much for using MineAdmin.
 -
 - @Author X.Mo<root@imoi.cn>
 - @Link   https://gitee.com/xmo/mineadmin-vue
-->
<template>
  <div class="block">
    <div class="user-header rounded-sm text-center">
      <div class="pt-6 mx-auto avatar-box">
        <!-- <ma-upload v-model="userInfo.avatar" rounded /> -->
        <a-upload
          action="/"
          :fileList="avatarFile ? [avatarFile] : []"
          :show-file-list="false"
          accept=".jpg,jpeg,.gif,.png,.svg,.bpm"
          @change="onChange"
          @before-upload="beforeUpload"
          :custom-request="customRequest"
        >
          <template #upload-button>
            <a-avatar :size="96" trigger-type="mask">
              <img
                v-if="userInfo.avatar"
                :src="userInfo.avatar"
                style="object-fit: cover"
              />
              <template v-else>
                {{ userInfo.nickname.slice(0, 1) }}
              </template>
              <template #trigger-icon>
                <IconEdit />
              </template>
            </a-avatar>
          </template>
        </a-upload>
      </div>
      <div>
        <a-tag size="large" class="mt-4 rounded-full" color="#165dff">
          {{
            (userStore.user && userStore.user.nickname) ||
            (userStore.user && userStore.user.username)
          }}
        </a-tag>
      </div>
    </div>

    <a-layout-content class="block lg:flex lg:justify-between">
      <div class="ma-content-block w-full mt-3 p-4">
        <a-tabs type="rounded">
          <a-tab-pane key="info" title="个人资料">
            <user-infomation />
          </a-tab-pane>
          <a-tab-pane key="safe" title="安全设置">
            <modify-password />
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-layout-content>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { useUserStore } from "@/store";
import { Message } from "@arco-design/web-vue";
import user from "@/api/system/user";
import commonApi from "@/api/common";

import ModifyPassword from "./components/modifyPassword.vue";
import UserInfomation from "./components/userInfomation.vue";

const userStore = useUserStore();
const userInfo = reactive({
  ...userStore.user,
});

onMounted(() => {});

userInfo.avatar = userStore?.user?.avatar ?? undefined;

const avatarFile = ref();

const onChange = (_, currentFile) => {
  avatarFile.value = {
    ...currentFile,
  };
};
const beforeUpload = (file) => {
  if (file.size > 1024 * 1024 * 2) {
    Message.error("图片大小不能超过2M");
    return false;
  } else {
    return true;
  }
};
const customRequest = async (options) => {
  const { fileItem } = options;
  const data = new FormData();
  data.append("file", fileItem.file);
  const resData = await commonApi.uploadAvatar(data);
  if (resData.code == 0) {
    Message.success("修改成功");
    const res = await userStore.requestUserInfo();
    userInfo.avatar = userStore.user.avatar;
  }
};
</script>
<script>
export default { name: "userCenter" };
</script>

<style scoped>
.avatar-box {
  width: 130px;
}
.user-header {
  width: 100%;
  height: 200px;
  background: url("@/assets/userBanner.jpg") no-repeat;
  background-size: cover;
}
</style>
