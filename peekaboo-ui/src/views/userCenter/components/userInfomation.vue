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
  <a-form class="w-full md:w-full mt-3" :model="userInfo" @submit="modifyInfo">
    <a-form-item label="账户名" label-col-flex="80px">
      <a-input disabled :default-value="userInfo.username" allow-clear />
    </a-form-item>
    <a-form-item label="昵称" label-col-flex="80px">
      <a-input v-model="userInfo.nickname" allow-clear />
    </a-form-item>
    <a-form-item label="手机" label-col-flex="80px">
      <a-input v-model="userInfo.phone" allow-clear />
    </a-form-item>
    <a-form-item label="邮箱" label-col-flex="80px">
      <a-input v-model="userInfo.email" allow-clear />
    </a-form-item>
    <a-form-item label="备注" label-col-flex="80px">
      <a-textarea
        v-model="userInfo.remark"
        :max-length="255"
        class="h-28"
        show-word-limit
        allow-clear
      />
    </a-form-item>
    <a-form-item label-col-flex="80px">
      <a-button html-type="submit" type="primary">保存</a-button>
    </a-form-item>
  </a-form>
</template>

<script setup>
import { reactive } from "vue";
import { useUserStore } from "@/store";
import { Message } from "@arco-design/web-vue";
import user from "@/api/system/user";

const userStore = useUserStore();

const userInfo = reactive({
  ...userStore.user,
});

const modifyInfo = async (data) => {
  console.log(data);
  const requestData = {
    nickname: data.values.nickname,
    mobile: data.values.phone,
    email: data.values.email,
    remark: data.values.remark,
  };
  const response = await user.updateInfo(requestData);
  if (response.code == 0) {
    Message.success("保存成功");
    await userStore.requestUserInfo();
    // userStore.user = data.values;
    return;
  }
};
</script>
