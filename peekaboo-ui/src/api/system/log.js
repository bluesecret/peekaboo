/*
 * @Author: wangyuanjie
 * @Date: 2024-04-07 18:27:07
 * @LastEditTime: 2024-04-08 15:11:16
 * @LastEditors: wangyuanjie
 */
import { request } from "@/utils/request.js";

export default {
  /**
   * 获取日志分页列表
   * @returns
   */
  getPageList(data = {}) {
    return request({
      url: "log/getPageSystemLogList",
      method: "post",
      data,
    });
  },

  /**
   * 删除日志
   * @returns
   */
  realDelete(params = {}) {
    return request({
      url: "log/delete",
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
      url: "log/findById",
      method: "get",
      params,
    });
  },
};
