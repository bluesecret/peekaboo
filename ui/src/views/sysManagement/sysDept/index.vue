<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:20:40
 * @LastEditTime: 2024-04-15 11:59:56
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
                  <a-form-item field="name" label="部门名称" show-colon>
                    <a-input
                      v-model="searchForm.name"
                      placeholder="请输入部门名称"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="status" label="部门状态" show-colon>
                    <a-select
                      v-model="searchForm.status"
                      placeholder="请选择部门状态"
                      allow-clear
                    >
                      <a-option :value="0">启用</a-option>
                      <a-option :value="1">禁用</a-option>
                    </a-select>
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
              <a-button type="primary" @click="toAdd" v-auth="['sysDept:add']">
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
                v-auth="['sysDept:delete']"
              >
                <template #icon>
                  <icon-delete />
                </template>
                删除
              </a-button>
              <a-button type="secondary" @click="handlerExpand">
                <template #icon>
                  <icon-expand v-if="!expandState" />
                  <icon-shrink v-else />
                </template>
                {{ expandState ? " 折叠" : " 展开" }}
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
            :row-selection="rowSelection"
            v-model:selectedKeys="selectedKeys"
            :pagination="false"
            :scroll="{ x: '110%', y: 'calc(100vh - 260px)' }"
          >
            <template #sort="{ record }">
              <a-input-number
                :default-value="record.sort"
                mode="button"
                @blur="changeSort($event, record.id)"
                :min="0"
                :max="1000"
              />
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
            <template #optional="{ record }">
              <a-space wrap align="center" class="justify-center">
                <a-button
                  type="text"
                  @click="toEdit(record.id)"
                  v-auth="['sysDept:update']"
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
                    v-auth="['sysDept:delete']"
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
    <editModal
      ref="editModalRef"
      :editForm="modalForm"
      :depts="depts"
      :userList="userList"
      @refresh="refresh"
    ></editModal>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import dept from "@/api/system/dept";
import commonApi from "@/api/common";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import { Message, Modal } from "@arco-design/web-vue";
import editModal from "./components/editModal.vue";

const searchForm = reactive({
  name: "",
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
const expandState = ref(false);
const depts = ref([]);
const userList = ref([]);
const searchFormRef = ref(null);
const tableContentRef = ref(null);
const { loading, setLoading } = useLoading(false);
const tableColumns = reactive(
  useTableColumn([
    {
      title: "部门名称",
      key: "name",
      dataIndex: "name",
      width: 120,
    },
    {
      title: "部门代码",
      key: "code",
      dataIndex: "code",
      width: 120,
    },
    {
      title: "负责人",
      key: "leaderUserName",
      dataIndex: "leaderUserName",
      width: 120,
    },
    {
      title: "手机号",
      key: "phone",
      dataIndex: "phone",
      width: 120,
    },
    {
      title: "排序",
      key: "sort",
      dataIndex: "sort",
      width: 100,
      slotName: "sort",
    },
    {
      title: "状态",
      dataIndex: "status",
      key: "status",
      width: 100,
      slotName: "status",
    },
    {
      title: "操作",
      dataIndex: "optional",
      key: "optional",
      width: 100,
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
const modalForm = ref({
  parentId: undefined,
  name: "",
  code: "",
  phone: "",
  status: 0,
  leaderUserId: undefined,
  sort: 1,
  remark: "",
});
const editModalRef = ref(null);

onMounted(() => {
  dept.tree().then((res) => {
    res.data.map((item) => {
      depts.value.push(item);
    });
  });
  commonApi.getUserList().then((res) => {
    res.data.map((item) => {
      userList.value.push(item);
    });
  });
  search();
});

const search = () => {
  const params = JSON.parse(JSON.stringify(searchForm));
  setLoading(true);
  dept
    .getList(params)
    .then((res) => {
      dataList.value = res.data;
    })
    .finally(() => {
      setLoading(false);
    });
};

const reset = () => {
  searchFormRef.value.resetFields();
  search();
};

const onUpdateTable = (newColumns) => {
  sortColumns(tableColumns, newColumns);
};
const toAdd = () => {
  modalForm.value = {
    parentId: undefined,
    name: "",
    code: "",
    phone: "",
    status: 0,
    leaderUserId: undefined,
    sort: 1,
    remark: "",
  };
  editModalRef.value.openModal(1);
};
const toDelete = (done) => {
  Modal.warning({
    title: "删除部门",
    content: "确定删除选中的部门吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toDelApi(selectedKeys.value, 2));
    },
    onCancel: () => {},
  });
};
const handlerExpand = () => {
  expandState.value = !expandState.value;
  expandState.value
    ? tableContentRef.value.expandAll(true)
    : tableContentRef.value.expandAll(false);
};
const toEdit = (id) => {
  dept.read({ id }).then((res) => {
    if (res.code == 0) {
      const editData = res.data;
      modalForm.value = editData;
      modalForm.value.parentId == 0
        ? (modalForm.value.parentId = undefined)
        : (modalForm.value.parentId = editData.parentId);
      editModalRef.value.openModal(2);
    }
  });
};
const changeStatus = async (e, id) => {
  const resData = await dept.update({ id, status: e });
  if (resData.code == 0) {
    search(false);
    return true;
  }
  return false;
};
const onDeleteSingle = (done, id) => {
  done(toDelApi(id, 1));
};
const changeSort = async (e, id) => {
  await dept.update({ id, sort: e.target.value });
  search();
};
const toDelApi = async (ids, type) => {
  const params = {};
  if (type == 1) {
    params.id = ids;
  } else {
    params.id = ids.join(",");
  }
  try {
    const resData = await dept.realDeletes(params);
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
      // Message.error(resData.msg);
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
  depts.value = [];
  dept.tree().then((res) => {
    res.data.map((item) => {
      depts.value.push(item);
    });
  });
};
</script>

<script>
export default { name: "sysDept" };
</script>

<style scoped></style>
