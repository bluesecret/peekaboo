<!--
 * @Author: wangyuanjie
 * @Date: 2024-04-16 09:40:41
 * @LastEditTime: 2024-04-16 09:40:42
 * @LastEditors: wangyuanjie
-->
<template>
  <div class="w-full mx-auto">
    <div class="block lg:grid lg:grid-cols-4 lg:gap-1">
      <a-card
        style="height: 65px"
        class="rounded-sm mt-3"
        :body-style="{ padding: 0, height: '65px' }"
        :bordered="false"
        hoverable
      >
        <div class="flex justify-between h-full">
          <div class="en-title bg-blue-600">NU</div>
          <div class="w-full ml-3.5 flex justify-between items-center">
            用户统计
            <a-space size="large" class="mr-3">
              <div class="text-right">
                <div>总数</div>
                <div>100</div>
              </div>
              <div class="text-right">
                <div>新增</div>
                <div><icon-caret-up class="text-green-600" /> 15</div>
              </div>
            </a-space>
          </div>
        </div>
      </a-card>
      <a-card
        style="height: 65px"
        class="rounded-sm mt-3 lg:ml-2"
        :body-style="{ padding: 0, height: '65px' }"
        :bordered="false"
        hoverable
      >
        <div class="flex justify-between h-full">
          <div class="en-title bg-red-600">NA</div>
          <div class="w-full ml-3.5 flex justify-between items-center">
            附件统计
            <a-space size="large" class="mr-3">
              <div class="text-right">
                <div>总数</div>
                <div>100</div>
              </div>
              <div class="text-right">
                <div>新增</div>
                <div><icon-caret-down class="text-red-600" /> 15</div>
              </div>
            </a-space>
          </div>
        </div>
      </a-card>
      <a-card
        style="height: 65px"
        class="rounded-sm mt-3 lg:ml-2"
        :body-style="{ padding: 0, height: '65px' }"
        :bordered="false"
        hoverable
      >
        <div class="flex justify-between h-full">
          <div class="en-title bg-pink-600">LC</div>
          <div class="w-full ml-3.5 flex justify-between items-center">
            登录统计
            <a-space size="large" class="mr-3">
              <div class="text-right">
                <div>总数</div>
                <div>100</div>
              </div>
              <div class="text-right">
                <div>新增</div>
                <div><icon-caret-down class="text-red-600" /> 15</div>
              </div>
            </a-space>
          </div>
        </div>
      </a-card>
      <a-card
        style="height: 65px"
        class="rounded-sm mt-3 lg:ml-2"
        :body-style="{ padding: 0, height: '65px' }"
        :bordered="false"
        hoverable
      >
        <div class="flex justify-between h-full">
          <div class="en-title bg-green-600">OC</div>
          <div class="w-full ml-3.5 flex justify-between items-center">
            操作统计
            <a-space size="large" class="mr-3">
              <div class="text-right">
                <div>总数</div>
                <div>100</div>
              </div>
              <div class="text-right">
                <div>新增</div>
                <div><icon-caret-up class="text-green-600" /> 15</div>
              </div>
            </a-space>
          </div>
        </div>
      </a-card>
    </div>
  </div>
</template>

<style scoped>
.en-title {
  width: 75px;
  color: #fff;
  text-align: center;
  line-height: 65px;
  font-weight: bold;
  font-size: 1.3em;

  border-radius: 2px 0 0 2px;
}
</style>
