import { request } from "@/utils/request.js";

export default {
  /**
   * 获取菜单树
   * @returns
   */
  getList(params = {}) {
    return request({
      url: "menu/getSystemMenuTreeList",
      method: "get",
      params,
    });
  },
  /**
   * 获取菜单树
   * @returns
   */
  getTree(params = {}) {
    return request({
      url: "menu/getMenuListTree",
      method: "get",
      params,
    });
  },

  /**
   * 添加菜单
   * @returns
   */
  save(params = {}) {
    return request({
      url: "menu/addSystemMenu",
      method: "post",
      data: params,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDeletes(params = {}) {
    return request({
      url: "menu/deleteSystemMenu",
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
      url: "menu/updateSystemMenu",
      method: "post",
      data,
    });
  },

  /**
   * 获取菜单详情
   * @returns
   */
  read(params = {}) {
    return request({
      url: "menu/getSystemDetails",
      method: "get",
      params,
    });
  },

  /**
   * 更改菜单状态
   * @returns
   */
  changeStatus(data = {}) {
    return request({
      url: "system/menu/changeStatus",
      method: "put",
      data,
    });
  },
};
