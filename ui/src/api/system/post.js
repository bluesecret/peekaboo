import { request } from "@/utils/request.js";

export default {
  /**
   * 获取岗位分页列表
   * @returns
   */
  getPageList(data = {}) {
    return request({
      url: "post/getSystemPost",
      method: "post",
      data,
    });
  },

  /**
   * 获取岗位列表
   * @returns
   */
  getList(params = {}) {
    return request({
      url: "system/post/list",
      method: "get",
      params,
    });
  },

  /**
   * 添加岗位
   * @returns
   */
  save(params = {}) {
    return request({
      url: "post/addSystemPost",
      method: "post",
      data: params,
    });
  },

  /**
   * 获取详情
   * @returns
   */
  read(params) {
    return request({
      url: "post/getSystemPostDetails",
      method: "get",
      params,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDeletes(params) {
    return request({
      url: "post/deleteSystemPost",
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
      url: "post/updateSystemPost",
      method: "post",
      data,
    });
  },

  /**
   * 更改岗位状态
   * @returns
   */
  changeStatus(data = {}) {
    return request({
      url: "post/updateSystemPostStatus",
      method: "post",
      data,
    });
  },
};
