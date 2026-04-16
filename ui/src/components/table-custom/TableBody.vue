<!--
 * @Author: wangyuanjie
 * @Date: 2024-03-28 11:29:36
 * @LastEditTime: 2024-03-28 11:29:37
 * @LastEditors: wangyuanjie
-->
<template>
  <a-card :bodyStyle="{ padding: '0 10px 10px 10px' }" :bordered="false">
    <slot name="header"></slot>
    <slot name="default"></slot>
    <div class="mt-1"></div>
    <slot name="footer"></slot>
  </a-card>
</template>

<script>
import { defineComponent } from "vue";
export default defineComponent({
  name: "TableBody",
});
</script>
