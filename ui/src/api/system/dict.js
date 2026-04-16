import { request } from "@/utils/request.js";

export const dictType = {
  /**
   * 获取字典类型，无分页
   * @returns
   */
  getTypePageList(data = {}) {
    return request({
      url: "type/getSystemDictTypeList",
      method: "post",
      data,
    });
  },

  /**
   * 添加字典类型
   * @returns
   */
  save(params = {}) {
    return request({
      url: "type/addSystemDictType",
      method: "post",
      data: params,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDelete(params) {
    return request({
      url: "type/deleteSystemDictType",
      method: "get",
      params,
    });
  },

  /**
   * 更新数据
   * @returns
   */
  update(data = {}) {
    return request({
      url: "type/updateSystemDictType",
      method: "post",
      data,
    });
  },

  /**
   * 获取字典类型详情
   * @returns
   */
  read(params = {}) {
    return request({
      url: "type/getSystemDictTypeDetails",
      method: "get",
      params,
    });
  },
};

export const dict = {
  /**
   * 快捷查询字典
   * @param {*} params
   * @returns
   */
  getDataPageList(data) {
    return request({
      url: "dict/getSystemDictDataList",
      method: "post",
      data,
    });
  },

  /**
   * 添加字典数据
   * @returns
   */
  saveDictData(data = {}) {
    return request({
      url: "dict/addSystemDictData",
      method: "post",
      data,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDeletesDictData(params) {
    return request({
      url: "dict/deleteSystemDictData",
      method: "get",
      params,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  readDictData(params) {
    return request({
      url: "dict/getSystemDictDataDetails",
      method: "get",
      params,
    });
  },

  /**
   * 更新数据
   * @returns
   */
  updateDictData(data = {}) {
    return request({
      url: "dict/updateSystemDictData",
      method: "post",
      data,
    });
  },
};
