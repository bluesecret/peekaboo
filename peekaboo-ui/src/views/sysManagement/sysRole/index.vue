<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:20:37
 * @LastEditTime: 2024-04-15 12:00:39
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
                  <a-form-item field="name" label="角色名称" show-colon>
                    <a-input
                      v-model="searchForm.name"
                      placeholder="请输入角色名称"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="code" label="角色标识" show-colon>
                    <a-input
                      v-model="searchForm.code"
                      placeholder="请输入角色标识"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="status" label="角色状态" show-colon>
                    <a-select
                      v-model="searchForm.status"
                      placeholder="请选择角色状态"
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
              <a-button type="primary" @click="toAdd" v-auth="['sysRole:add']">
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
                v-auth="['sysRole:delete']"
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
          >
            <template #dataScope="{ record }">
              <a-tag color="arcoblue">{{
                formatList("data_scoped", record.dataScope)
              }}</a-tag>
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
                  v-auth="['sysRole:update']"
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
                    v-auth="['sysRole:delete']"
                  >
                    <template #icon>
                      <icon-delete />
                    </template>
                    删除
                  </a-button>
                </a-popconfirm>
                <a-dropdown
                  @select="
                    (key) => {
                      handleSelect(key, record);
                    }
                  "
                  :popup-max-height="false"
                >
                  <a-button type="text">
                    <template #icon>
                      <icon-more />
                    </template>
                    更多
                  </a-button>
                  <template #content>
                    <a-doption v-auth="['sysRole:bindUser']"
                      >绑定用户</a-doption
                    >
                    <a-doption v-auth="['sysRole:bindMenu']"
                      >菜单权限</a-doption
                    >
                  </template>
                </a-dropdown>
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
      @refresh="refresh"
    ></editModal>
    <userModal
      ref="userModalRef"
      :selectedKey
      :bindUserRoleId
      @refresh="refresh"
    ></userModal>
    <menuModal
      ref="menuModalRef"
      :bindMenuRoleId
      @refresh="refresh"
    ></menuModal>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import role from "@/api/system/role";
import dept from "@/api/system/dept";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import { Message, Modal } from "@arco-design/web-vue";
import editModal from "./components/editModal.vue";
import useListFormat from "@/hooks/dictionaries";
import userModal from "./components/userModal.vue";
import menuModal from "./components/menuModal.vue";

const searchForm = reactive({
  name: "",
  code: "",
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
const searchFormRef = ref(null);
const tableContentRef = ref(null);
const depts = ref([]);
const { loading, setLoading } = useLoading(false);
const { formatList } = useListFormat();
const tableColumns = reactive(
  useTableColumn([
    {
      title: "角色名称",
      key: "name",
      dataIndex: "name",
      width: 100,
    },
    {
      title: "角色标识",
      key: "code",
      dataIndex: "code",
      width: 100,
    },
    {
      title: "数据权限",
      key: "dataScope",
      dataIndex: "dataScope",
      slotName: "dataScope",
      width: 100,
    },
    {
      title: "排序",
      key: "sort",
      dataIndex: "sort",
      width: 80,
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
  name: "",
  code: "",
  dataScope: undefined,
  status: 0,
  dataScopeDeptIds: [],
  sort: 1,
  remark: "",
});
const editModalRef = ref(null);
const userModalRef = ref(null);
const menuModalRef = ref(null);
const selectedKey = ref([]);
const bindUserRoleId = ref({});
const bindMenuRoleId = ref({});

onMounted(() => {
  dept.tree().then((res) => {
    res.data.map((item) => {
      depts.value.push(item);
    });
  });
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
  setLoading(true);
  role
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

const toAdd = () => {
  modalForm.value = {
    name: "",
    code: "",
    dataScope: undefined,
    status: 0,
    dataScopeDeptIds: [],
    sort: 1,
    remark: "",
  };
  editModalRef.value.openModal(1);
};

const toDelete = (done) => {
  Modal.warning({
    title: "删除角色",
    content: "确定删除选中的角色吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toDelApi(selectedKeys.value, 2));
    },
    onCancel: () => {},
  });
};

const changeSort = async (e, id) => {
  await role.update({ id, sort: e.target.value });
  search(false);
};

const toEdit = (id) => {
  role.read({ id }).then((res) => {
    if (res.code == 0) {
      const editData = res.data;
      modalForm.value = editData;
      editModalRef.value.openModal(2);
    }
  });
};

const changeStatus = async (e, id) => {
  const resData = await role.changeStatus({ id, status: e });
  if (resData.code == 0) {
    return true;
  }
  return false;
};

const onDeleteSingle = (done, id) => {
  done(toDelApi(id, 1));
};

const handleSelect = (selection, record) => {
  switch (selection) {
    case "绑定用户":
      toBindUser(record);
      break;
    case "菜单权限":
      toBindMenuPermission(record);
      break;
    default:
  }
};

const toBindUser = (record) => {
  const params = { id: record.id };
  role.getBindUserList(params).then((res) => {
    if (res.code == 0) {
      selectedKey.value = res.data.map((item) => item.id);
      bindUserRoleId.value = {
        id: record.id,
        roleName: record.name,
        roleCode: record.code,
      };
      userModalRef.value.openModal();
    }
  });
};

const toBindMenuPermission = (record) => {
  bindMenuRoleId.value = {
    id: record.id,
    roleName: record.name,
    roleCode: record.code,
  };
  menuModalRef.value.openModal();
};

const toDelApi = async (ids, type) => {
  const params = {};
  if (type == 1) {
    params.id = ids;
  } else {
    params.id = ids.join(",");
  }
  try {
    const resData = await role.realDeletes(params);
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
};
</script>

<script>
export default { name: "sysRole" };
</script>

<style scoped></style>
