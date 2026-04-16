<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-11 11:46:50
 * @LastEditTime: 2024-04-15 14:23:18
 * @LastEditors: wangyuanjie
-->
<template>
  <a-modal
    v-model:visible="setFormVisible"
    @cancel="handleCancel"
    :on-before-ok="handleBeforeOk"
    width="700px"
  >
    <template #title>
      {{ modalType == 1 ? "新增" : "编辑" }}
    </template>
    <a-form
      ref="modalFormRef"
      :model="modalForm"
      :label-col-props="{ span: 5 }"
      :wrapper-col-props="{ span: 19 }"
      label-align="right"
      :rules="modalFormRules"
    >
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="name" label="任务名称" show-colon>
            <a-input
              v-model.trim="modalForm.name"
              placeholder="请输入任务名称"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="cronExpression"
            tooltip="Cron表达式需完整，例如：0 0 0 * * ? *，表示每天执行，前面*初需有值"
            label="Cron表达式"
            show-colon
          >
            <a-popover
              v-model:popup-visible="CronObj.cronPopover"
              position="bottom"
              width="700px"
              trigger="click"
            >
              <a-input
                v-model="modalForm.cronExpression"
                placeholder="* * * * * ? *"
                allow-clear
              ></a-input>
              <template #content>
                <vueCron
                  v-if="setFormVisible"
                  :cron-value="CronObj"
                  @change-cron="changeCron"
                  @close="closePopover"
                ></vueCron>
              </template>
            </a-popover>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="className"
            tooltip="后台须继承BasicJob对象，通过代码扫描获取该定时任务类"
            label="调用方法"
            show-colon
          >
            <a-select
              v-model="modalForm.className"
              placeholder="请选择调用方法"
              allow-clear
              allow-search
            >
              <a-option
                v-for="item in classNameList"
                :key="item.value"
                :value="item.value"
              >
                {{ item.label + "：" + item.value }}
              </a-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="dateRange" label="执行时间" show-colon>
            <a-range-picker
              showTime
              v-model="modalForm.dateRange"
              @change="onChangeDate"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="status" label="状态" show-colon>
            <a-radio-group v-model="modalForm.status">
              <a-radio :value="0">正常</a-radio>
              <a-radio :value="1">禁用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <!-- <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="type" label="执行策略" show-colon>
            <a-radio-group v-model="modalForm.type" type="button">
              <a-radio value="1">不调度</a-radio>
              <a-radio value="3">立即执行</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row> -->
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, watch, reactive } from "vue";
import scheduledtask from "@/api/system/scheduledtask";
import { Message } from "@arco-design/web-vue";
import dayjs from "dayjs";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      name: "",
      className: undefined,
      cronExpression: undefined,
      status: 0,
      type: "3",
      param: null,
      dateRange: [dayjs().format("YYYY-MM-DD HH:mm:ss"), "2099-12-30 23:59:59"],
    },
  },
});
const emit = defineEmits();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  name: "",
  className: undefined,
  cronExpression: undefined,
  status: 0,
  type: "3",
  param: null,
  dateRange: [dayjs().format("YYYY-MM-DD HH:mm:ss"), "2099-12-30 23:59:59"],
});
const modalFormRules = {
  name: [
    {
      required: true,
      message: "角色名称不能为空",
    },
  ],
  className: [
    {
      required: true,
      message: "调用方法不能为空",
    },
  ],
  cronExpression: [
    {
      required: true,
      message: "Cron表达式不能为空",
      trigger: "change",
    },
  ],
  dateRange: [
    {
      required: true,
      message: "执行时间范围不能为空",
    },
  ],
};
const classNameList = ref([]);
const CronObj = reactive({
  cronPopover: false,
  cron: "",
  userSetting: null,
});

const openModal = async (type) => {
  modalType.value = type;
  try {
    const resData = await scheduledtask.getClassApi();
    if (resData.code == 0) {
      classNameList.value = resData.data.map((item) => {
        return {
          label: item.description,
          value: item.className,
        };
      });
      CronObj.cronPopover = false;
      CronObj.cron = modalForm.value.cronExpression;
      CronObj.userSetting = modalForm.value.param
        ? JSON.parse(modalForm.value.param)
        : null;
      setFormVisible.value = true;
    }
  } catch (error) {
    console.log(error);
  }
};
defineExpose({ openModal });

const changeCron = (arr) => {
  [CronObj.cron, CronObj.userSetting] = arr;
  modalForm.value.cronExpression = CronObj.cron;
  modalForm.value.param = JSON.stringify(CronObj.userSetting);
  modalFormRef.value.clearValidate("cronExpression");
};

const closePopover = () => {
  CronObj.cronPopover = false;
};

const onChangeDate = (val) => {
  if (val) {
    modalForm.value.startTime = val[0];
    modalForm.value.endTime = val[1];
  } else {
    modalForm.value.startTime = null;
    modalForm.value.endTime = null;
  }
};

const handleCancel = () => {
  modalFormRef.value.resetFields();
  setFormVisible.value = false;
};

const handleBeforeOk = async (done) => {
  // console.log(avatarFile.value.file);
  const validate = await modalFormRef.value.validate();
  if (validate) {
    return done(false);
  }
  const data = JSON.parse(JSON.stringify(modalForm.value));

  if (!data.startTime || !data.endTime) {
    data.startTime = data.dateRange[0];
    data.endTime = data.dateRange[1];
  }
  try {
    if (modalType.value === 1) {
      const resData = await scheduledtask.addOrUpateApi(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await scheduledtask.addOrUpateApi(data);
      if (resData.code == 0) {
        Message.success("修改成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    }
  } catch {
    done(false);
  } finally {
    emit("refresh");
  }
};
watch(
  () => props.editForm,
  (val) => {
    modalForm.value = val;
  },
  {
    deep: true,
    immediate: true,
  }
);
</script>

<script>
export default { name: "sysScheduledTask:editModal" };
</script>

<style lang="less" scoped></style>
