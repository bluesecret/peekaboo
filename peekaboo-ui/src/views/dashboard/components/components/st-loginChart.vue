<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-16 09:38:20
 * @LastEditTime: 2024-04-16 09:56:15
 * @LastEditors: wangyuanjie
-->
<template>
  <div class="ma-content-block p-3 mt-3">
    <h1 class="mb-3 ml-2 ma-title">登录统计</h1>
    <ma-chart height="300px" :option="loginChartOptions" />
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from "vue";
import { graphic } from "echarts";

function graphicFactory(side) {
  return {
    type: "text",
    bottom: "8",
    ...side,
    style: {
      text: "",
      textAlign: "center",
      fill: "#4E5969",
      fontSize: 12,
    },
  };
}

const xAxis = ref([
  "2024-04-06",
  "2024-04-07",
  "2024-04-08",
  "2024-04-09",
  "2024-04-10",
  "2024-04-11",
  "2024-04-12",
  "2024-04-13",
  "2024-04-14",
  "2024-04-15",
]);
const chartsData = ref([32, 56, 61, 89, 12, 33, 56, 92, 180, 25]);
const graphicElements = ref([
  graphicFactory({ left: "2.6%" }),
  graphicFactory({ right: 0 }),
]);

const loginChartOptions = ref({
  grid: {
    left: "2.6%",
    right: "0",
    top: "10",
    bottom: "30",
  },
  xAxis: {
    type: "category",
    offset: 2,
    data: xAxis.value,
    boundaryGap: false,
    axisLabel: {
      color: "#4E5969",
      formatter(value, idx) {
        if (idx === 0) return "";
        if (idx === xAxis.value.length - 1) return "";
        return `${value}`;
      },
    },
    axisLine: {
      show: false,
    },
    axisTick: {
      show: false,
    },
    splitLine: {
      show: true,
      interval: (idx) => {
        if (idx === 0) return false;
        if (idx === xAxis.value.length - 1) return false;
        return true;
      },
      lineStyle: {
        color: "#E5E8EF",
      },
    },
    axisPointer: {
      show: true,
      lineStyle: {
        color: "#23ADFF",
        width: 2,
      },
    },
  },
  yAxis: {
    type: "value",
    axisLine: {
      show: false,
    },
    axisLabel: {
      formatter(value, idx) {
        if (idx === 0) return value;
        return `${value}`;
      },
    },
    splitLine: {
      show: true,
      lineStyle: {
        type: "dashed",
        color: "#E5E8EF",
      },
    },
  },
  tooltip: {
    trigger: "axis",
    formatter(params) {
      return `<div class="login-chart">
          <p class="tooltip-title">${params[0].axisValueLabel}</p>
          <div class="content-panel"><span>登录次数</span><span class="tooltip-value">${Number(
            params[0].value
          ).toLocaleString()}</span></div>
        </div>`;
    },
  },
  graphic: {
    elements: graphicElements.value,
  },
  series: [
    {
      data: chartsData.value,
      type: "line",
      smooth: true,
      symbolSize: 12,
      emphasis: {
        focus: "series",
        itemStyle: {
          borderWidth: 2,
        },
      },
      lineStyle: {
        width: 3,
        color: new graphic.LinearGradient(0, 0, 1, 0, [
          {
            offset: 0,
            color: "rgba(30, 231, 255, 1)",
          },
          {
            offset: 0.5,
            color: "rgba(36, 154, 255, 1)",
          },
          {
            offset: 1,
            color: "rgba(111, 66, 251, 1)",
          },
        ]),
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
        color: new graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: "rgba(17, 126, 255, 0.16)",
          },
          {
            offset: 1,
            color: "rgba(17, 128, 255, 0)",
          },
        ]),
      },
    },
  ],
});
</script>

<style scoped lang="less">
.ma-title {
  font-size: 16px;
  color: var(--color-text-1);
}
</style>
