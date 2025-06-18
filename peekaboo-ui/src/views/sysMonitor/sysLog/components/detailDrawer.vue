<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-08 15:18:14
 * @LastEditTime: 2024-04-08 19:36:34
 * @LastEditors: wangyuanjie
-->
<template>
  <a-drawer
    v-model:visible="setFormVisible"
    @cancel="handleCancel"
    :footer="false"
    width="700px"
    title="日志详情"
  >
    <ma-info
      :columns="columns"
      :column="1"
      :data="logData"
      layout="horizontal"
    ></ma-info>
    <a-collapse
      :default-active-key="['request', 'response']"
      expand-icon-position="right"
      class="mt-3"
    >
      <a-collapse-item header="请求数据" key="request">
        <ma-code-editor
          v-model="logData.requestParam"
          v-if="setFormVisible"
          :height="150"
          readonly
        />
      </a-collapse-item>
      <a-collapse-item header="响应数据" key="response">
        <ma-code-editor
          v-model="logData.result"
          v-if="setFormVisible"
          :height="150"
          readonly
        />
      </a-collapse-item>
    </a-collapse>
  </a-drawer>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import MaInfo from "@/components/ma-info/index.vue";
import { formatJson } from "@/utils/common";

const setFormVisible = ref(false);
const logData = ref({});
const columns = reactive([
  {
    title: "请求路由",
    dataIndex: "requestUrl",
    key: "requestUrl",
  },
  {
    title: "操作用户",
    dataIndex: "username",
    key: "username",
  },
  {
    title: "请求方式",
    dataIndex: "requestType",
    key: "requestType",
  },
  {
    title: "请求耗时（毫秒）",
    dataIndex: "costTime",
    key: "costTime",
  },
  {
    title: "业务名称",
    dataIndex: "logContent",
    key: "logContent",
  },
  {
    title: "操作时间",
    dataIndex: "startTime",
    key: "startTime",
  },
  {
    title: "用户IP",
    dataIndex: "ipAddr",
    key: "ipAddr",
  },
]);

const openModal = async (data) => {
  logData.value = data;
  if (data.requestParam && data.requestParam.length > 0) {
    logData.value.requestParam = !/\\\\/g.test(data.requestParam)
      ? formatJson(logData.value.requestParam)
      : logData.value.requestParam.replace(/,/g, ",\n");
  }
  if (data.result && data.result.length > 0) {
    logData.value.result = !/\\\\/g.test(data.result)
      ? formatJson(logData.value.result)
      : logData.value.result.replace(/,/g, "\n");
  }
  setFormVisible.value = true;
};
defineExpose({ openModal });

const handleCancel = () => {
  //   modalFormRef.value.resetFields();
  setFormVisible.value = false;
};

const handleBeforeOk = async (done) => {
  done(true);
};
</script>

<script>
export default { name: "sysLog:detailDrawer" };
</script>

<style lang="less" scoped></style>
