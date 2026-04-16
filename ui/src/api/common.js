/*
 * @Author: wangyuanjie
 * @Date: 2024-03-27 17:09:00
 * @LastEditTime: 2024-04-16 10:42:42
 * @LastEditors: wangyuanjie
 */
import { request } from "@/utils/request.js";

export default {
  /**
   * 获取用户列表(不分页)
   * @returns
   */
  getUserList(params = {}) {
    return request({
      url: "user/getSysUserList",
      method: "get",
      params,
    });
  },

  /**
   * 通过id 列表获取用户基础信息
   * @returns
   */
  getUserInfoByIds(data = {}) {
    return request({
      url: "system/common/getUserInfoByIds",
      method: "post",
      data,
    });
  },

  /**
   * 获取部门列表
   * @returns
   */
  getDeptTreeList(params = {}) {
    return request({
      url: "system/common/getDeptTreeList",
      method: "get",
      params,
    });
  },

  /**
   * 获取角色列表
   * @returns
   */
  getRoleList(params = {}) {
    return request({
      url: "role/getRoleList",
      method: "get",
      params,
    });
  },

  /**
   * 获取岗位列表
   * @returns
   */
  getPostList(params = {}) {
    return request({
      url: "post/getPostList",
      method: "get",
      params,
    });
  },

  /**
   * 获取菜单树形列表
   * @returns
   */
  getMenuTreeList(params = {}) {
    return request({
      url: "menu/getSystemMenuTreeStatusList",
      method: "get",
      params,
    });
  },

  /**
   * 清除所有缓存
   * @returns
   */
  clearAllCache() {
    return request({
      url: "system/common/clearAllCache",
      method: "get",
    });
  },

  /**
   * 上传图片接口
   * @returns
   */
  uploadImage(data = {}) {
    return request({
      url: "system/uploadImage",
      method: "post",
      timeout: 30000,
      // headers: { 'Content-Type': 'multipart/form-data' },
      data,
    });
  },

  /**
   * 上传文件接口
   * @returns
   */
  uploadFile(data = {}) {
    return request({
      url: "system/uploadFile",
      method: "post",
      timeout: 30000,
      // headers: { 'Content-Type': 'multipart/form-data' },
      data,
    });
  },

  /**
   * 分片上传接口
   * @returns
   */
  chunkUpload(data = {}) {
    return request({
      url: "system/chunkUpload",
      method: "post",
      timeout: 30000,
      // headers: { 'Content-Type': 'multipart/form-data' },
      data,
    });
  },

  /**
   * 保存网络图片
   * @returns
   */
  saveNetWorkImage(data = {}) {
    return request({
      url: "system/saveNetworkImage",
      method: "post",
      data,
    });
  },

  /**
   * 获取资源列表
   */
  getResourceList(params = {}) {
    return request({
      url: "system/common/getResourceList",
      method: "get",
      params,
    });
  },

  /**
   * 通用导入Excel
   */
  importExcel(url, data) {
    return request({
      url,
      method: "post",
      data,
      timeout: 30 * 1000,
      // headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  /**
   * 下载通用方法
   */
  download(url, method = "post") {
    return request({ url, method, responseType: "blob" });
  },

  /**
   * 快捷查询字典
   */
  getDict(code) {
    return request({
      url: "system/dataDict/list?code=" + code,
      method: "get",
    });
  },

  /**
   * 快捷查询多个字典
   */
  getDicts(codes) {
    return request({
      url: "system/dataDict/lists?codes=" + codes.join(","),
      method: "get",
    });
  },

  /**
   * 快捷查询所有字典
   */
  getDictList() {
    return request({
      url: "type/getAllTypeDataList",
      method: "get",
    });
  },

  getFileInfoById(id) {
    return request({
      url: "system/getFileInfoById?id=" + id,
      method: "get",
    });
  },

  getFileInfoByHash(hash) {
    return request({
      url: "system/getFileInfoByHash?hash=" + hash,
      method: "get",
    });
  },

  /**
   * 个人中心-修改头像
   * @returns
   */
  uploadAvatar(data = {}) {
    return request({
      url: "/user/updateAvatar",
      method: "post",
      timeout: 30000,
      // headers: { 'Content-Type': 'multipart/form-data' },
      data,
    });
  },
};
