/*
 * @Author: wangyuanjie
 * @Date: 2024-04-08 10:13:56
 * @LastEditTime: 2024-04-08 16:16:10
 * @LastEditors: wangyuanjie
 */
import { defineStore } from "pinia";
import commonApi from "@/api/common";

const useDictStore = defineStore("dict", {
  state: () => ({
    dict: undefined,
  }),
  persist: true,

  actions: {
    setDictList(list) {
      this.dict = list;
    },
    requestDictList() {
      return commonApi
        .getDictList()
        .then((res) => {
          if (res.code == "0") {
            this.setDictList(res.data);
            return true;
          } else {
            return false;
          }
        })
        .catch((e) => {
          return false;
        });
    },
  },
});

export default useDictStore;
