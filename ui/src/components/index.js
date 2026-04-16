import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import {
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  GaugeChart,
} from "echarts/charts";
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
} from "echarts/components";

import MaCrud from "./ma-crud/index.vue";
import MaForm from "./ma-form/index.vue";
import MaChart from "./ma-charts/index.vue";
import MaUpload from "./ma-upload/index.vue";
import MaTreeSlider from "./ma-treeSlider/index.vue";
import MaResource from "./ma-resource/index.vue";
import MaResourceButton from "./ma-resource/button.vue";
import MaUser from "./ma-user/index.vue";
import MaEditor from "./ma-editor/index.vue";
import MaWangEditor from "./ma-wangEditor/index.vue";
import MaIcon from "./ma-icon/index.vue";
import MaCodeEditor from "./ma-codeEditor/index.vue";
import MaUserInfo from "./ma-userInfo/index.vue";
import MaCityLinkage from "./ma-cityLinkage/index.vue";

import TableBody from "./table-custom/TableBody.vue";
import TableConfig from "./table-custom/TableConfig.vue";
import TableFooter from "./table-custom/TableFooter.vue";
import TableHeader from "./table-custom/TableHeader.vue";
import SortableTable from "./table-custom/SortableTable.vue";

import VueCron from "./vue3-cron/index.vue";
import DictSelect from "./dict-select/index.vue";

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  GaugeChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
]);

export default {
  install(Vue) {
    Vue.component("MaChart", MaChart);
    Vue.component("MaCrud", MaCrud);
    Vue.component("MaForm", MaForm);
    Vue.component("MaUpload", MaUpload);
    Vue.component("MaTreeSlider", MaTreeSlider);
    Vue.component("MaResource", MaResource);
    Vue.component("MaResourceButton", MaResourceButton);
    Vue.component("MaUser", MaUser);
    Vue.component("MaEditor", MaEditor);
    Vue.component("MaWangEditor", MaWangEditor);
    Vue.component("MaIcon", MaIcon);
    Vue.component("MaCodeEditor", MaCodeEditor);
    Vue.component("MaUserInfo", MaUserInfo);
    Vue.component("MaCityLinkage", MaCityLinkage);
    Vue.component("SortableTable", SortableTable);
    Vue.component("TableBody", TableBody);
    Vue.component("TableFooter", TableFooter);
    Vue.component("TableConfig", TableConfig);
    Vue.component("TableHeader", TableHeader);
    Vue.component("VueCron", VueCron);
    Vue.component("DictSelect", DictSelect);
  },
};
