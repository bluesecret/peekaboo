import { request } from "@/utils/request.js";

export default {
  /**
   * 获取用户
   * @returns
   */
  getPageList(params = {}) {
    return request({
      url: "user/getSystemUserList",
      method: "post",
      data: params,
    });
  },

  /**
   * 读取一个用户
   * @returns
   */
  read(params) {
    return request({
      url: "user/getSystemUserDetails",
      method: "get",
      params,
    });
  },

  /**
   * 添加用户
   * @returns
   */
  save(params = {}) {
    return request({
      url: "user/addSystemUser",
      method: "post",
      data: params,
    });
  },

  /**
   * 真实删除
   * @returns
   */
  realDeletes(params) {
    return request({
      url: "user/deleteSystemUserById",
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
      url: "user/updateSystemUser",
      method: "post",
      data,
    });
  },

  /**
   * 更改用户状态
   * @returns
   */
  changeStatus(data = {}) {
    return request({
      url: "user/updateUserStatus",
      method: "post",
      data,
    });
  },

  /**
   * 清除用户缓存
   * @returns
   */
  clearCache(params = {}) {
    return request({
      url: "system/user/clearCache",
      method: "post",
      data: params,
    });
  },

  /**
   * 设置用户首页
   * @returns
   */
  setHomePage(data = {}) {
    return request({
      url: "system/user/setHomePage",
      method: "post",
      data,
    });
  },

  /**
   * 初始化用户密码
   * @returns
   */
  initUserPassword(params) {
    return request({
      url: "user/resetPassword",
      method: "get",
      params,
    });
  },

  /**
   * 用户更新个人资料
   * @returns
   */
  updateInfo(data = {}) {
    return request({
      url: "user/updateUserInfo",
      method: "post",
      data,
    });
  },

  /**
   * 导入用户
   * @returns
   */
  importUser(data = {}) {
    return request({
      url: "user/import",
      method: "post",
      data,
    });
  },

  /**
   * 用户修改密码
   * @returns
   */
  modifyPassword(data = {}) {
    return request({
      url: "user/updatePassword",
      method: "post",
      data,
    });
  },
};
