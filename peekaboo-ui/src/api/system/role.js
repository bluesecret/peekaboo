import { request } from "@/utils/request.js";

export default {
  /**
   * 获取角色分页列表
   * @returns
   */
  getPageList(params = {}) {
    return request({
      url: "role/getSystemRoleList",
      method: "post",
      data: params,
    });
  },

  /**
   * 获取角色列表
   * @returns
   */
  getList(params = {}) {
    return request({
      url: "system/role/list",
      method: "get",
      params,
    });
  },

  /**
   * 通过角色获取菜单
   * @returns
   */
  getMenuByRole(id) {
    return request({
      url: "system/role/getMenuByRole/" + id,
      method: "get",
    });
  },

  /**
   * 通过角色获取部门
   * @returns
   */
  getDeptByRole(id) {
    return request({
      url: "system/role/getDeptByRole/" + id,
      method: "get",
    });
  },

  /**
   * 添加角色
   * @returns
   */
  save(data = {}) {
    return request({
      url: "role/addSystemRole",
      method: "post",
      data,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDeletes(params = {}) {
    return request({
      url: "role/deleteSystemRoleById",
      method: "get",
      params,
    });
  },

  /**
   * 读取角色
   * @returns
   */
  read(params = {}) {
    return request({
      url: "role/getSystemRoleDetails",
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
      url: "role/updateSystemRole",
      method: "post",
      data,
    });
  },

  updateMenuPermission(id, data) {
    return request({
      url: "system/role/menuPermission/" + id,
      method: "put",
      data,
    });
  },

  /**
   * 更改角色状态
   * @returns
   */
  changeStatus(params = {}) {
    return request({
      url: "role/updateRoleStatus",
      method: "post",
      data: params,
    });
  },

  /**
   * 获取角色绑定的用户列表
   * @returns
   */
  getBindUserList(params = {}) {
    return request({
      url: "role/getBindUser",
      method: "get",
      params,
    });
  },
  /**
   * 角色绑定用户
   * @returns
   */
  bindRoleUser(data = {}) {
    return request({
      url: "role/insertUserRole",
      method: "post",
      data,
    });
  },

  /**
   * 获取角色绑定的菜单列表
   * @returns
   */
  getBindMenuList(params = {}) {
    return request({
      url: "role/getBindMenu",
      method: "get",
      params,
    });
  },
  /**
   * 角色绑定菜单
   * @returns
   */
  bindRoleMenu(data = {}) {
    return request({
      url: "role/insertRoleMenu",
      method: "post",
      data,
    });
  },
};
