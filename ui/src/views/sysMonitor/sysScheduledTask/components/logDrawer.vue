<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-11 17:58:05
 * @LastEditTime: 2024-04-15 18:21:15
 * @LastEditors: wangyuanjie
-->
<template>
  <a-drawer
    v-model:visible="setFormVisible"
    @cancel="handleCancel"
    :footer="false"
    width="900px"
    title="执行日志"
  >
    <ma-info
      :columns="infoColumns"
      :column="1"
      :data="scheduledTaskData"
      layout="horizontal"
    ></ma-info>
    <div class="mt-2">
      <a-table
        stripe
        :loading="loading"
        :columns="tableColumns"
        :data="dataList"
        :pagination="pagination"
        row-key="id"
        @page-change="pageChange"
        @page-size-change="pageSizeChange"
      >
        <template #result="{ record }">
          <div v-if="record.result && record.result === 'success'">
            <a-tag color="green">成功</a-tag>
          </div>
          <div v-else>
            <a-tag color="red">失败</a-tag>
          </div>
        </template>
      </a-table>
    </div>
  </a-drawer>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import MaInfo from "@/components/ma-info/index.vue";
import useLoading from "@/hooks/loading";
import scheduledtask from "@/api/system/scheduledtask";

const { loading, setLoading } = useLoading(false);
const setFormVisible = ref(false);
const scheduledTaskData = ref({});
const infoColumns = reactive([
  {
    title: "任务名称",
    dataIndex: "name",
    key: "name",
  },
  {
    title: "定时规则",
    dataIndex: "cronExpression",
    key: "cronExpression",
  },
  {
    title: "调用目标",
    dataIndex: "className",
    key: "className",
  },
]);

const tableColumns = ref([
  {
    title: "执行时间",
    key: "startTime",
    dataIndex: "startTime",
    width: 100,
  },
  {
    title: "耗时（毫秒）",
    key: "cons",
    dataIndex: "cons",
    width: 100,
  },
  {
    title: "执行结果",
    key: "result",
    dataIndex: "result",
    slotName: "result",
    width: 100,
  },
  {
    title: "异常信息",
    key: "message",
    dataIndex: "message",
    ellipsis: true,
    tooltip: true,
    width: 100,
  },
]);
let dataList = ref([]);
const pagination = reactive({
  total: 0,
  current: 1,
  pageSize: 10,
  showPageSize: true,
  pageSizeOptions: [10, 20, 50],
  showJumper: true,
  showTotal: true,
});

const openModal = async (data) => {
  scheduledTaskData.value = data;
  await search(true);
  setFormVisible.value = true;
};
defineExpose({ openModal });

const search = async (flag) => {
  const params = {
    id: scheduledTaskData.value.id,
  };
  params.pageSize = pagination.pageSize;
  if (flag) {
    params.currentPage = pagination.current = 1;
  } else {
    params.currentPage = pagination.current;
  }
  setLoading(true);
  scheduledtask
    .taskLogPageList(params)
    .then((res) => {
      pagination.total = res.data.total;
      dataList.value = res.data.rows;
    })
    .finally(() => {
      setLoading(false);
    });
};

const pageChange = (current) => {
  pagination.current = current;
  search();
};

const pageSizeChange = (size) => {
  pagination.pageSize = size;
  search();
};

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
