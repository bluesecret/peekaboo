/*
 * @Author: wangyuanjie
 * @Date: 2024-04-11 09:29:05
 * @LastEditTime: 2024-04-11 19:11:57
 * @LastEditors: wangyuanjie
 */
import { request } from "@/utils/request.js";

export default {
  /**
   * 获取定时任务分页列表
   * @returns
   */
  getPageList(data = {}) {
    return request({
      url: "job/getPageList",
      method: "post",
      data,
    });
  },

  /**
   * 删除定时任务
   * @returns
   */
  realDelete(params = {}) {
    return request({
      url: "job/deleteById",
      method: "get",
      params,
    });
  },

  /**
   * 根据id查询日志详情
   * @returns
   */
  read(params = {}) {
    return request({
      url: "job/findById",
      method: "get",
      params,
    });
  },

  /**
   * 新增或修改定时任务
   * @returns
   */
  addOrUpateApi(data = {}) {
    return request({
      url: "job/createOrUpdate",
      method: "post",
      data,
    });
  },

  /**
   * 新增或修改定时任务
   * @returns
   */
  getClassApi() {
    return request({
      url: "job/scanForJobClasses",
      method: "get",
    });
  },

  /**
   * 修改定时任务状态
   * @returns
   */
  changeStatus(params) {
    return request({
      url: "job/updateStatus",
      method: "get",
      params,
    });
  },

  /**
   * 立即执行定时任务
   * @returns
   */
  executionNow(params) {
    return request({
      url: "job/now",
      method: "get",
      params,
    });
  },
  /**
   * 立即执行定时任务
   * @returns
   */
  taskLogPageList(params) {
    return request({
      url: "job/getJobDetil",
      method: "get",
      params,
    });
  },
};
