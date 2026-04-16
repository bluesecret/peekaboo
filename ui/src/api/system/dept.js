import { request } from "@/utils/request.js";

export default {
  /**
   * 获取部门树
   * @returns
   */
  getList(params = {}) {
    return request({
      url: "dept/getSysDepartmentDetailList",
      method: "get",
      params,
    });
  },

  /**
   * 获取部门选择树
   * @returns
   */
  tree() {
    return request({
      url: "dept/getSysDepartmentListStatus",
      method: "get",
    });
  },

  /**
   * 添加部门
   * @returns
   */
  save(params = {}) {
    return request({
      url: "dept/addDepartment",
      method: "post",
      data: params,
    });
  },

  /**
   * 部门详情
   * @returns
   */
  read(params = {}) {
    return request({
      url: "dept/findById",
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
      url: "dept/deleteDepartment",
      method: "get",
      params,
    });
  },

  /**
   * 更新数据
   * @returns
   */
  update(data) {
    return request({
      url: "dept/uploadSysDepartment",
      method: "post",
      data,
    });
  },

  /**
   * 数字运算操作
   * @returns
   */
  numberOperation(data = {}) {
    return request({
      url: "system/dept/numberOperation",
      method: "put",
      data,
    });
  },

  /**
   * 更改部门状态
   * @returns
   */
  changeStatus(data = {}) {
    return request({
      url: "system/dept/changeStatus",
      method: "put",
      data,
    });
  },
};
