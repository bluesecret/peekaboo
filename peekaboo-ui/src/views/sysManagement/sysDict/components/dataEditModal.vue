<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-07 11:43:56
 * @LastEditTime: 2024-05-23 10:54:59
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
      :label-col-props="{ span: 4 }"
      :wrapper-col-props="{ span: 20 }"
      label-align="right"
      :rules="modalFormRules"
    >
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="pid" label="上级字典数据" show-colon>
            <!-- <a-input
              v-model.trim="modalForm.label"
              placeholder="请选择上级字典数据"
              allow-clear
            /> -->
            <a-tree-select
              :fieldNames="{
                key: 'id',
                title: 'label',
                children: 'children',
              }"
              :data="treeData"
              v-model="modalForm.pid"
              placeholder="请选择上级字典数据"
            ></a-tree-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="label" label="字典标签" show-colon>
            <a-input
              v-model.trim="modalForm.label"
              placeholder="请输入字典标签"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="value" label="字典键值" show-colon>
            <a-input
              v-model.trim="modalForm.value"
              placeholder="请输入字典键值"
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="sort" label="排序" show-colon>
            <a-input-number
              v-model.trim="modalForm.sort"
              placeholder="请输入排序"
              :min="0"
              :max="1000"
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
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item
            field="colorType"
            label="标签颜色"
            show-colon
            tooltip="在表格中展示Tag标签颜色,可输入对应颜色的英文或HEX值"
          >
            <a-space>
              <a-tag
                style="cursor: pointer"
                v-for="item in defaultTagList"
                :color="item"
                :bordered="modalForm.colorType == item"
                @click="changeColor(item)"
                :key="item"
                >{{ item }}</a-tag
              >
              <a-trigger
                position="bottom"
                trigger="hover"
                auto-fit-position
                :unmount-on-close="false"
              >
                <a-tag
                  style="cursor: pointer"
                  @click="changeColor(customColor)"
                  :bordered="modalForm.colorType == customColor"
                  :color="customColor ? customColor : ''"
                  >{{ customColor ? customColor : "自定义颜色" }}</a-tag
                >
                <template #content>
                  <ColorPicker
                    theme="dark"
                    :color="customColor"
                    :sucker-hide="true"
                    :colors-default="defaultColorList"
                    @changeColor="selectColor"
                    style="width: 218px"
                  />
                </template>
              </a-trigger>
            </a-space>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item field="remark" label=" 备注" show-colon>
            <a-textarea
              v-model="modalForm.remark"
              placeholder="请输入备注"
              :max-length="{ length: 250, errorOnly: false }"
              show-word-limit
              allow-clear
              :auto-size="{
                minRows: 2,
                maxRows: 5,
              }"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, watch, reactive, computed } from "vue";
import { dict } from "@/api/system/dict";
import { Message } from "@arco-design/web-vue";
import { ColorPicker } from "vue-color-kit";
import "vue-color-kit/dist/vue-color-kit.css";

const props = defineProps({
  editForm: {
    type: Object,
    default: {
      label: "",
      value: "",
      status: 0,
      sort: 1,
      remark: "",
    },
  },
  dictTypeStr: {
    type: String,
    default: "",
  },
  dataList: {
    type: Array,
    default: [],
  },
});
const emit = defineEmits();

const setFormVisible = ref(false);
const modalFormRef = ref(null);
const modalType = ref(1);
const modalForm = ref({
  label: "",
  value: "",
  status: 0,
  sort: 1,
  remark: "",
  colorType: "",
});
const modalFormRules = {
  label: [
    {
      required: true,
      message: "字典标签不能为空",
    },
  ],
  value: [
    {
      required: true,
      message: "字典键值不能为空",
    },
  ],
};
const defaultTagList = ref(["red", "gold", "cyan", "arcoblue", "magenta"]);
const customColor = ref(null);

const defaultColorList = reactive([
  "#165DFF",
  "#F53F3F",
  "#F77234",
  "#F7BA1E",
  "#00B42A",
  "#14C9C9",
  "#3491FA",
  "#722ED1",
  "#F5319D",
  "#D91AD9",
  "#34C759",
  "#43a047",
  "#7cb342",
  "#c0ca33",
  "#86909c",
  "#6d4c41",
]);

const openModal = (type) => {
  modalType.value = type;
  setFormVisible.value = true;
};
defineExpose({ openModal });

const changeColor = (item) => {
  // modalForm.value.colorType = item;
  if (modalForm.value.colorType == item) {
    modalForm.value.colorType = "";
  } else {
    modalForm.value.colorType = item;
  }
};

const selectColor = (color) => {
  customColor.value = color.hex;
  modalForm.value.colorType = color.hex;
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
  data.dictType = props.dictTypeStr;
  try {
    if (modalType.value === 1) {
      const resData = await dict.saveDictData(data);
      if (resData.code == 0) {
        Message.success("新增成功");
        modalFormRef.value.resetFields();
        done(true);
      } else {
        done(false);
      }
    } else {
      const resData = await dict.updateDictData(data);
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
    const flag = defaultTagList.value.indexOf(modalForm.value.colorType) !== -1;
    if (flag) {
      customColor.value = null;
    } else {
      customColor.value = modalForm.value.colorType;
    }
  },
  {
    deep: true,
    immediate: true,
  }
);
//递归处理树形数据，添加disabled属性
const deepTreeData = (tree, id) => {
  let arr = [];
  tree.forEach((item) => {
    item.disabled = false;
    if (item.id === id) {
      item.disabled = true;
    }
    if (item.children) {
      deepTreeData(item.children, id);
    }
    arr.push(item);
  });
  return arr;
};
const treeData = computed(() => {
  return deepTreeData(props.dataList, props.editForm.id);
});
</script>

<script>
export default { name: "sysDict:dictData:dataEditModal" };
</script>

<style lang="less" scoped></style>
