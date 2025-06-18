<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:18:38
 * @LastEditTime: 2024-05-28 14:25:49
 * @LastEditors: wangyuanjie
-->
<template>
  <div class="ma-content-block lg:flex justify-between p-4">
    <div class="lg:w-2/12 w-full h-full p-2 shadow">
      <ma-tree-slider
        :data="depts"
        searchPlaceholder="搜索部门"
        v-model="defaultKey"
        @click="switchDept"
      />
    </div>
    <div class="lg:w-10/12 w-full lg:ml-4 mt-5 lg:mt-0">
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
                  <a-form-item field="username" label="用户名" show-colon>
                    <a-input
                      v-model="searchForm.username"
                      placeholder="请输入用户名"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item field="status" label="用户状态" show-colon>
                    <a-select
                      v-model="searchForm.status"
                      placeholder="请选择用户状态"
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
              <a-button type="primary" @click="toAdd" v-auth="['sysUser:add']">
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
                v-auth="['sysUser:delete']"
              >
                <template #icon>
                  <icon-delete />
                </template>
                删除
              </a-button>

              <a-upload
                action="/"
                ref="uploadRef"
                :show-file-list="false"
                :custom-request="uploadFileHandler"
                accept="
                  application/vnd.ms-excel,
                  application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
                "
              >
                <template #upload-button>
                  <a-dropdown-button
                    :button-props="{
                      loading: uploadLoading,
                    }"
                    trigger="hover"
                  >
                    <icon-upload class="mr-2" /> 上传
                    <template #icon>
                      <icon-down />
                    </template>
                    <template #content>
                      <a-doption @click.prevent="toDownloadTem">
                        下载模板
                      </a-doption>
                    </template>
                  </a-dropdown-button>
                </template>
              </a-upload>
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
            :pagination="pagination"
            row-key="id"
            :row-selection="rowSelection"
            v-model:selectedKeys="selectedKeys"
            :scroll="{ x: '110%' }"
            @page-change="pageChange"
            @page-size-change="pageSizeChange"
          >
            <template #avatar="{ record }">
              <a-avatar>
                <img
                  v-if="record.avatar"
                  :src="record.avatar"
                  style="object-fit: cover"
                />
                <template v-else>
                  {{ record.nickname.slice(0, 1) }}
                </template>
              </a-avatar>
            </template>
            <template #deptNames="{ record }">
              <a-space wrap class="justify-center">
                <a-tag
                  v-if="
                    record.deptNames && record.deptNames.split(',').length > 0
                  "
                  color="blue"
                >
                  {{ record.deptNames.split(",")[0] }}
                </a-tag>
                <a-tooltip
                  v-if="
                    record.deptNames && record.deptNames.split(',').length > 1
                  "
                  :content="record.deptNames"
                >
                  <a-tag color="blue">...</a-tag>
                </a-tooltip>
              </a-space>
            </template>
            <template #roleNames="{ record }">
              <a-space wrap align="center" class="justify-center">
                <a-tag
                  v-if="
                    record.roleNames && record.roleNames.split(',').length > 0
                  "
                  color="arcoblue"
                >
                  {{ record.roleNames.split(",")[0] }}
                </a-tag>
                <a-tooltip
                  v-if="
                    record.roleNames && record.roleNames.split(',').length > 1
                  "
                  :content="record.roleNames"
                >
                  <a-tag color="arcoblue">...</a-tag>
                </a-tooltip>
              </a-space>
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
                  v-auth="['sysUser:update']"
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
                    v-auth="['sysUser:delete']"
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
                      handleSelect(key, record.id);
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
                    <a-doption v-auth="['sysUser:resetPwd']"
                      >重置密码</a-doption
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
      :roleList="roleList"
      :postList="postList"
      @refresh="refresh"
    ></editModal>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue";
import dept from "@/api/system/dept";
import user from "@/api/system/user";
import commonApi from "@/api/common";
import { useTableColumn } from "@/hooks/table";
import useLoading from "@/hooks/loading";
import { sortColumns } from "@/utils/common";
import tool from "@/utils/tool";
import { Message, Modal } from "@arco-design/web-vue";
import editModal from "./components/editModal.vue";

const searchForm = reactive({
  username: "",
  status: undefined,
});
const searchFormRef = ref(null);
const depts = ref([{ label: "所有部门", value: "0" }]);
const toolsSetting = ref({
  refresh: {
    show: true,
    time: 2000,
  },
  searchShowOrHide: true,
  tablePrint: true,
  tableSort: true,
});
const roleList = ref([]);
const postList = ref([]);
const defaultKey = ref(["0"]);
const { loading, setLoading } = useLoading(true);
const tableColumns = reactive(
  useTableColumn([
    {
      title: "头像",
      dataIndex: "avatar",
      key: "avatar",
      width: 75,
      slotName: "avatar",
    },
    {
      title: "用户名",
      key: "username",
      dataIndex: "username",
      width: 120,
    },
    {
      title: "昵称",
      key: "nickname",
      dataIndex: "nickname",
      width: 120,
    },
    {
      title: "部门",
      key: "deptNames",
      dataIndex: "deptNames",
      width: 140,
      slotName: "deptNames",
      ellipsis: true,
    },
    {
      title: "角色",
      dataIndex: "roleNames",
      key: "roleNames",
      width: 140,
      slotName: "roleNames",
      ellipsis: true,
    },
    {
      title: "手机号",
      dataIndex: "mobile",
      key: "mobile",
      width: 100,
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
      width: 210,
      slotName: "optional",
      fixed: "right",
    },
  ])
);
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
const rowSelection = {
  type: "checkbox",
  showCheckedAll: true,
};
const selectedKeys = ref([]);
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
const editModalRef = ref(null);
const uploadLoading = ref(false);
const tableContentRef = ref(null);

onMounted(async () => {
  if (
    depts.value.length == 1 &&
    roleList.value.length == 0 &&
    postList.value.length == 0
  ) {
    await initData();
  }
  await search(true);
});

const initData = async () => {
  await dept.tree().then((res) => {
    depts.value = [{ label: "所有部门", value: "0" }];
    res.data.map((item) => {
      depts.value.push(item);
    });
  });
  await commonApi.getRoleList().then((res) => {
    roleList.value = [];
    res.data.map((item) => {
      roleList.value.push(item);
    });
  });
  await commonApi.getPostList().then((res) => {
    postList.value = [];
    res.data.map((item) => {
      postList.value.push(item);
    });
  });
};

const search = (flag) => {
  const params = JSON.parse(JSON.stringify(searchForm));
  params.pageSize = pagination.pageSize;
  if (flag) {
    params.currentPage = pagination.current = 1;
  } else {
    params.currentPage = pagination.current;
  }
  params.deptId = defaultKey.value[0];
  setLoading(true);
  user
    .getPageList(params)
    .then((res) => {
      dataList.value = [];
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

const switchDept = (id) => {
  defaultKey.value = id;
  search(true);
};
const onUpdateTable = (newColumns) => {
  sortColumns(tableColumns, newColumns);
};
const handleSelect = (selection, id) => {
  switch (selection) {
    case "重置密码":
      toInitPassword(id);
      break;
    default:
  }
};
const toAdd = () => {
  modalForm.value = {
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
  };
  editModalRef.value.openModal(1);
};
const toDelete = (done) => {
  Modal.warning({
    title: "删除用户",
    content: "确定删除选中的用户吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toDelApi(selectedKeys.value, 2));
    },
    onCancel: () => {},
  });
};

const uploadFileHandler = (options) => {
  if (!options.fileItem) return;
  const file = options.fileItem.file;
  if (file.size > 10 * 1024 * 1024) {
    Message.warning(file.name + " " + t("upload.sizeLimit"));
    return;
  }
  const data = new FormData();
  data.append("file", file);
  uploadLoading.value = true;
  commonApi
    .importExcel("user/import", data)
    .then((res) => {
      if (res.code == 0) {
        Message.success("导入成功");
        search(true);
      }
    })
    .finally(() => {
      uploadLoading.value = false;
    });
};

const toDownloadTem = () => {
  commonApi
    .download("user/export")
    .then((res) => {
      tool.download(res);
      Message.success("请求成功，文件开始下载");
    })
    .catch(() => {
      Message.error("请求服务器错误，下载失败");
    });
};

const toEdit = (id) => {
  user.read({ id }).then((res) => {
    if (res.code == 0) {
      const editData = res.data;
      editData.deptIds = editData.deptIds && editData.deptIds.split(",");
      editData.roleIds = editData.roleIds && editData.roleIds.split(",");
      editData.postIds = editData.postIds && editData.postIds.split(",");
      modalForm.value = editData;
      editModalRef.value.openModal(2);
    }
  });
};
const changeStatus = async (e, id) => {
  const resData = await user.changeStatus({ id, status: e });
  if (resData.code == 0) {
    return true;
  }
  return false;
};
const onDeleteSingle = (done, id) => {
  done(toDelApi(id, 1));
};
const toInitPassword = (id) => {
  Modal.warning({
    title: "重置密码",
    content: "确定重置该用户的密码吗？",
    okText: "确定",
    cancelText: "取消",
    hideCancel: false,
    onBeforeOk: (done) => {
      done(toInitPWApi(id));
    },
    onCancel: () => {},
  });
};
const toDelApi = async (ids, type) => {
  const params = {};
  if (type == 1) {
    params.id = ids;
  } else {
    params.id = ids.join(",");
  }
  try {
    const resData = await user.realDeletes(params);
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
const toInitPWApi = async (id) => {
  const params = { id };
  try {
    const resData = await user.initUserPassword(params);
    if (resData.code == 0) {
      Message.success("重置成功");
      selectedKeys.value = [];
      search(false);
      return true;
    } else {
      // Message.error(resData.msg);
      return false;
    }
  } catch (error) {
    return false;
  }
};
const refresh = () => {
  nextTick(() => {
    search(false);
  });
  // search(false);
};
</script>

<script>
export default { name: "sysUser" };
</script>

<style lang="less" scoped></style>
