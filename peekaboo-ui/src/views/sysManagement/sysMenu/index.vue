<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:20:38
 * @LastEditTime: 2024-04-15 12:00:14
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
                  <a-form-item field="name" label="菜单名称" show-colon>
                    <a-input
                      v-model="searchForm.name"
                      placeholder="请输入菜单名称"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="status" label="菜单状态" show-colon>
                    <a-select
                      v-model="searchForm.status"
                      placeholder="请选择菜单状态"
                      allow-clear
                    >
                      <a-option :value="0">启用</a-option>
                      <a-option :value="1">禁用</a-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="6">
                  <a-form-item field="visible" label="是否可见" show-colon>
                    <a-select
                      v-model="searchForm.visible"
                      placeholder="请选择是否可见"
                      allow-clear
                    >
                      <a-option :value="0">可见</a-option>
                      <a-option :value="1">隐藏</a-option>
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
                @click="toAdd(null)"
                v-auth="['sysMenu:add']"
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
                v-auth="['sysMenu:delete']"
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
            <template #type="{ record }">
              <a-tag color="blue" v-if="record.type == 1">菜单</a-tag>
              <a-tag color="green" v-else-if="record.type == 2">按钮</a-tag>
              <a-tag color="orangered" v-else-if="record.type == 3">外链</a-tag>
              <a-tag color="pinkpurple" v-else>Iframe</a-tag>
            </template>
            <template #icon="{ record }">
              <component
                :is="record.icon"
                v-if="record.type != 2 && record.icon"
              />
              <span v-else>-</span>
            </template>
            <template #visible="{ record }">
              <a-tag
                color="arcoblue"
                v-if="record.type != 2 && record.visible == 0"
                >可见</a-tag
              >
              <a-tag color="magenta" v-else-if="record.type != 2">隐藏</a-tag>
              <span v-else>-</span>
            </template>
            <template #path="{ record }">
              <span v-if="record.type != 2">{{ record.path }}</span>
              <span v-else>-</span>
            </template>
            <template #component="{ record }">
              <span v-if="record.type != 2">{{ record.component }}</span>
              <span v-else>-</span>
            </template>
            <template #sort="{ record }">
              <a-input-number
                :default-value="record.sort"
                mode="button"
                @blur="changeSort($event, record.id)"
                :min="0"
                :max="1000"
                v-if="record.type != 2"
              />
              <span v-else>-</span>
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
                  status="success"
                  @click="toAdd(record.id)"
                  v-if="record.type == 1"
                  v-auth="['sysMenu:add']"
                >
                  <template #icon>
                    <icon-plus />
                  </template>
                  新增
                </a-button>
                <a-button
                  type="text"
                  @click="toEdit(record.id)"
                  v-auth="['sysMenu:update']"
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
                    v-auth="['sysMenu:delete']"
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
      @refresh="refresh"
    ></editModal>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import menu from "@/api/system/menu";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import { Message, Modal } from "@arco-design/web-vue";
import editModal from "./components/editModal.vue";

const searchForm = reactive({
  name: "",
  status: undefined,
  visible: undefined,
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
const searchFormRef = ref(null);
const tableContentRef = ref(null);
const { loading, setLoading } = useLoading(false);
const tableColumns = reactive(
  useTableColumn([
    {
      title: "菜单名称",
      key: "name",
      dataIndex: "name",
      width: 120,
    },
    {
      title: "菜单类型",
      key: "type",
      dataIndex: "type",
      slotName: "type",
      width: 80,
    },
    {
      title: "菜单图标",
      key: "icon",
      dataIndex: "icon",
      width: 60,
      slotName: "icon",
    },
    {
      title: "是否可见",
      key: "visible",
      dataIndex: "visible",
      width: 80,
      slotName: "visible",
    },
    {
      title: "菜单标识",
      key: "permission",
      dataIndex: "permission",
      width: 80,
    },
    {
      title: "路由地址",
      key: "path",
      dataIndex: "path",
      width: 80,
      slotName: "path",
    },
    {
      title: "视图组件",
      key: "component",
      dataIndex: "component",
      width: 100,
      slotName: "component",
    },
    {
      title: "排序",
      key: "sort",
      dataIndex: "sort",
      width: 120,
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
      width: 160,
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
  type: 1,
  icon: "",
  status: 0,
  permission: "",
  path: "",
  component: "",
  visible: 0,
  keepAlive: 1,
  sort: 1,
  remark: "",
});
const editModalRef = ref(null);

onMounted(() => {
  search();
});

const search = () => {
  const params = JSON.parse(JSON.stringify(searchForm));
  setLoading(true);
  menu
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
const toAdd = (id = null) => {
  if (id) {
    modalForm.value = {
      parentId: undefined,
      name: "",
      type: 1,
      icon: "",
      status: 0,
      permission: "",
      path: "",
      component: "",
      visible: 0,
      keepAlive: 1,
      sort: 1,
      remark: "",
    };
    modalForm.value.parentId = id;
    console.log(modalForm.value);
  }
  editModalRef.value.openModal(1);
};
const toDelete = () => {
  Modal.warning({
    title: "删除菜单",
    content: "确定删除选中的菜单吗？",
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
  menu.read({ id }).then((res) => {
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
  const resData = await menu.update({ id, status: e });
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
  await menu.update({ id, sort: e.target.value });
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
    const resData = await menu.realDeletes(params);
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
export default { name: "sysMenu" };
</script>

<style scoped></style>
