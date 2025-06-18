<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-07 09:19:29
 * @LastEditTime: 2024-05-27 09:39:58
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
                <a-col :span="6">
                  <a-form-item field="label" label="字典标签" show-colon>
                    <a-input
                      v-model="searchForm.label"
                      placeholder="请输入字典标签"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="value" label="字典键值" show-colon>
                    <a-input
                      v-model="searchForm.value"
                      placeholder="请输入字典键值"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="status" label="状态" show-colon>
                    <a-select
                      v-model="searchForm.status"
                      placeholder="请选择字典数据状态"
                      allow-clear
                    >
                      <a-option :value="0">启用</a-option>
                      <a-option :value="1">禁用</a-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="6" style="text-align: right">
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
                type="primary"
                @click="toAdd"
                v-auth="['sysDict:dictData:add']"
              >
                <template #icon>
                  <icon-plus />
                </template>
                新增
              </a-button>
              <a-button
                status="danger"
                type="primary"
                :disabled="!selectedKeys.length"
                @click="toDelete"
                v-auth="['sysDict:dictData:delete']"
              >
                <template #icon>
                  <icon-delete />
                </template>
                删除
              </a-button>
              <a-button @click="toBack">
                <template #icon>
                  <icon-undo />
                </template>
                返回
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
          >
            <template #label="{ record }">
              <a-tag v-if="record.colorType" :color="record.colorType">{{
                record.label
              }}</a-tag>
              <span v-else>{{ record.label }}</span>
            </template>
            <template #status="{ record }">
              <a-switch
                :checked-value="0"
                :unchecked-value="1"
                :beforeChange="
                  (checked) => {
                    return changeStatus(checked, record.id);
                  }
                "
                v-model="record.status"
              />
            </template>
            <template #sort="{ record }">
              <a-input-number
                :default-value="record.sort"
                mode="button"
                @blur="changeSort($event, record.id)"
                :min="0"
                :max="1000"
              />
            </template>
            <template #optional="{ record }">
              <a-space wrap align="center" class="justify-center">
                <a-button
                  type="text"
                  @click="toEdit(record.id)"
                  v-auth="['sysDict:dictData:update']"
                >
                  <template #icon>
                    <icon-edit />
                  </template>
                  编辑
                </a-button>
                <a-popconfirm
                  content="确定删除该条数据吗?"
                  type="warning"
                  :on-before-ok="
                    (done) => {
                      onDeleteSingle(done, record.id);
                    }
                  "
                >
                  <a-button
                    type="text"
                    status="danger"
                    v-auth="['sysDict:dictData:delete']"
                  >
                    <template #icon>
                      <icon-delete />
                    </template>
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <dataEditModal
      ref="dataEditModalRef"
      :editForm="modalForm"
      :dictTypeStr="dictTypeStr"
      :dataList="dataList"
      @refresh="refresh"
    ></dataEditModal>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import { Message, Modal } from "@arco-design/web-vue";
import dataEditModal from "./components/dataEditModal.vue";
import { dict } from "@/api/system/dict";
import { useRoute, useRouter } from "vue-router";

const searchForm = reactive({
  label: "",
  value: "",
  status: undefined,
});
const toolsSetting = ref({
  refresh: {
    show: true,
    time: 2000,
  },
  searchShowOrHide: true,
  tablePrint: true,
  tableSort: true,
});
const dictTypeStr = ref("");
const searchFormRef = ref(null);
const tableContentRef = ref(null);
const { loading, setLoading } = useLoading(false);
const route = useRoute();
const router = useRouter();
const tableColumns = reactive(
  useTableColumn([
    {
      title: "字典标签",
      key: "label",
      dataIndex: "label",
      slotName: "label",
      width: 100,
    },
    {
      title: "字典键值",
      key: "value",
      dataIndex: "value",
      width: 100,
    },
    {
      title: "排序",
      dataIndex: "sort",
      key: "sort",
      width: 120,
      slotName: "sort",
    },
    {
      title: "状态",
      dataIndex: "status",
      key: "status",
      width: 90,
      slotName: "status",
    },
    {
      title: "创建时间",
      dataIndex: "createTime",
      key: "createTime",
      width: 120,
    },
    {
      title: "操作",
      dataIndex: "optional",
      key: "optional",
      width: 140,
      slotName: "optional",
      fixed: "right",
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
const modalForm = ref({
  label: "",
  value: "",
  status: 0,
  sort: 1,
  remark: "",
  colorType: "",
});
const dataEditModalRef = ref(null);

onMounted(() => {
  dictTypeStr.value = route.query.type;
  search();
});

const search = (flag) => {
  const params = JSON.parse(JSON.stringify(searchForm));
  params.pageSize = pagination.pageSize;
  if (flag) {
    params.currentPage = pagination.current = 1;
  } else {
    params.currentPage = pagination.current;
  }
  params.dictType = dictTypeStr.value;
  setLoading(true);
  dict
    .getDataPageList(params)
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

const toAdd = () => {
  modalForm.value = {
    label: "",
    value: "",
    status: 0,
    sort: 1,
    remark: "",
    colorType: "",
  };
  dataEditModalRef.value.openModal(1);
};

const toDelete = (done) => {
  Modal.warning({
    title: "删除该字典数据",
    content: "确定删除选中的字典数据吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toDelApi(selectedKeys.value, 2));
    },
    onCancel: () => {},
  });
};

const toBack = () => {
  router.push({
    name: "sysDict",
  });
};

const toEdit = (id) => {
  dict.readDictData({ id }).then((res) => {
    if (res.code == 0) {
      const editData = res.data;
      modalForm.value = editData;
      dataEditModalRef.value.openModal(2);
    }
  });
};

const changeStatus = async (e, id) => {
  const resData = await dict.updateDictData({ id, status: e });
  if (resData.code == 0) {
    return true;
  }
  return false;
};

const changeSort = async (e, id) => {
  await dict.updateDictData({ id, sort: e.target.value });
  search(false);
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
    const resData = await dict.realDeletesDictData(params);
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
  search();
};
</script>

<script>
export default { name: "sysDict:dictData" };
</script>

<style scoped></style>
