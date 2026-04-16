<!-- 
 * @Author: wangyuanjie
 * @Date: 2024-04-07 18:12:04
 * @LastEditTime: 2024-04-15 16:17:34
 * @LastEditors: wangyuanjie
-->
<template>
  <div class="ma-content-block lg:flex justify-between p-4">
    <div class="lg:w-12/12 w-full">
      <div class="searchForm">
        <a-row>
          <a-col :flex="1">
            <a-form
              ref="searchFormRef"
              :model="searchForm"
              :label-col-props="{ span: 7 }"
              :wrapper-col-props="{ span: 17 }"
              label-align="left"
            >
              <a-row :gutter="16">
                <a-col :span="8">
                  <a-form-item field="logType" label="日志类型" show-colon>
                    <dict-select
                      v-model="searchForm.logType"
                      dict-type="system_log_type"
                      placeholder="请选择日志类型"
                      allow-clear
                      allow-search
                      @change="handleSearch"
                    ></dict-select>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="nickname" label="操作人员" show-colon>
                    <a-input
                      v-model="searchForm.nickname"
                      placeholder="请输入操作人员"
                    ></a-input>
                  </a-form-item>
                </a-col>

                <a-col :span="8" style="text-align: right">
                  <a-space :size="10">
                    <a-button type="primary" @click="search">
                      <template #icon>
                        <icon-search />
                      </template>
                      查询
                    </a-button>
                    <a-button @click="reset">
                      <template #icon>
                        <icon-refresh />
                      </template>
                      重置
                    </a-button>
                  </a-space>
                </a-col>
              </a-row>
            </a-form>
          </a-col>
        </a-row>
      </div>
      <div class="table-block">
        <div class="table-header flex justify-between items-end">
          <div class="table-btn">
            <a-space>
              <a-button
                status="danger"
                type="primary"
                :disabled="!selectedKeys.length"
                @click="toDelete"
              >
                <template #icon>
                  <icon-delete />
                </template>
                删除
              </a-button>
            </a-space>
          </div>
          <SortableTable
            :setting="toolsSetting"
            :searchRef="searchFormRef"
            :tableRef="tableContentRef"
            :columns="tableColumns"
            @update="onUpdateTable"
            @refresh="refresh"
          />
        </div>
        <div class="table-content mt-2">
          <a-table
            ref="tableContentRef"
            stripe
            :loading="loading"
            :columns="tableColumns"
            :data="dataList"
            row-key="id"
            :pagination="pagination"
            :row-selection="rowSelection"
            v-model:selectedKeys="selectedKeys"
            @page-change="pageChange"
            @page-size-change="pageSizeChange"
            @row-dblclick="toDetail"
          >
            <template #logType="{ record }">
              <a-tag
                v-if="formatColor('system_log_type', record.logType)"
                :color="formatColor('system_log_type', record.logType)"
                >{{ formatList("system_log_type", record.logType) }}</a-tag
              >
              <a-tag v-else>
                {{ formatList("system_log_type", record.logType) }}
              </a-tag>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <detailDrawer ref="detailDrawerRef"></detailDrawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import { Message, Modal } from "@arco-design/web-vue";
import log from "@/api/system/log";
import useListFormat from "@/hooks/dictionaries";
import detailDrawer from "./components/detailDrawer.vue";

const searchForm = reactive({
  logType: undefined,
  nickname: "",
});
const toolsSetting = ref({
  refresh: {
    show: false,
    time: 2000,
  },
  searchShowOrHide: true,
  tablePrint: true,
  tableSort: true,
});
const searchFormRef = ref(null);
const tableContentRef = ref(null);
const detailDrawerRef = ref(null);
const { loading, setLoading } = useLoading(false);
const { formatList, formatColor, returnList } = useListFormat();
const tableColumns = reactive(
  useTableColumn([
    {
      title: "日志类型",
      key: "logType",
      dataIndex: "logType",
      slotName: "logType",
      width: 100,
    },
    {
      title: "日志内容",
      key: "logContent",
      dataIndex: "logContent",
      width: 100,
    },
    {
      title: "操作地址",
      dataIndex: "ipAddr",
      key: "ipAddr",
      width: 120,
      slotName: "status",
    },
    {
      title: "操作人员",
      dataIndex: "username",
      key: "username",
      width: 90,
      slotName: "status",
    },
    {
      title: "操作时间",
      dataIndex: "createTime",
      key: "createTime",
      width: 120,
    },
  ])
);
let dataList = ref([]);
const rowSelection = {
  type: "checkbox",
  showCheckedAll: true,
};
const selectedKeys = ref([]);
const pagination = reactive({
  total: 0,
  current: 1,
  pageSize: 10,
  showPageSize: true,
  pageSizeOptions: [10, 20, 50],
  showJumper: true,
  showTotal: true,
});

onMounted(() => {
  search();
});

const handleSearch = (val) => {
  console.log(val);
};

const search = (flag) => {
  const params = JSON.parse(JSON.stringify(searchForm));
  params.pageSize = pagination.pageSize;
  if (flag) {
    params.currentPage = pagination.current = 1;
  } else {
    params.currentPage = pagination.current;
  }
  setLoading(true);
  log
    .getPageList(params)
    .then((res) => {
      pagination.total = res.data.total;
      dataList.value = res.data.rows;
    })
    .finally(() => {
      setLoading(false);
    });
};

const reset = () => {
  searchFormRef.value.resetFields();
  search(true);
};

const pageChange = (current) => {
  pagination.current = current;
  search(false);
};

const pageSizeChange = (size) => {
  pagination.pageSize = size;
  search(true);
};

const onUpdateTable = (newColumns) => {
  sortColumns(tableColumns, newColumns);
};

const toDelete = (done) => {
  Modal.warning({
    title: "删除该字典",
    content: "确定删除选中的字典吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toDelApi(selectedKeys.value, 2));
    },
    onCancel: () => {},
  });
};

const toDetail = (record) => {
  const params = {
    id: record.id,
  };
  log.read(params).then((res) => {
    if (res.code == 0) {
      detailDrawerRef.value.openModal(res.data);
    }
  });
};

const onDeleteSingle = (done, id) => {
  done(toDelApi(id, 1));
};

const toDelApi = async (ids, type) => {
  const params = {};
  if (type == 1) {
    params.id = ids;
  } else {
    params.id = ids.join(",");
  }
  try {
    const resData = await log.realDelete(params);
    if (resData.code == 0) {
      Message.success("删除成功");
      if (type == 2) {
        selectedKeys.value = [];
      } else {
        selectedKeys.value = selectedKeys.value.filter((item) => item != ids);
      }
      search(false);
      return true;
    } else {
      return false;
    }
  } catch (error) {
    if (error.code == 401) {
      return true;
    } else {
      return false;
    }
  }
};
const refresh = () => {
  search(false);
};
</script>

<script>
export default { name: "sysLog" };
</script>

<style lang="less" scoped></style>
