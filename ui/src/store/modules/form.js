/*
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:10:02
 * @LastEditTime: 2024-03-27 17:10:03
 * @LastEditors: wangyuanjie
 */

import { defineStore } from "pinia";

let defaultConfig = {
  formList: [],
  crudList: {},
};

const useFormStore = defineStore("form", {
  state: () => ({ ...defaultConfig }),

  getters: {
    appCurrentConfig(state) {
      return { ...state };
    },
  },

  actions: {
    updateSettings(partial) {
      this.$patch(partial);
    },
  },
});

export default useFormStore;
