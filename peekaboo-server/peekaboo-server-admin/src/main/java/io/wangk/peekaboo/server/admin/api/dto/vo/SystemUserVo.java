package io.wangk.peekaboo.server.admin.api.dto.vo;

import cn.ruixi.azure.rainbow.boot.common.util.DateUtil;
import cn.ruixi.azure.rainbow.boot.module.system.handler.SexConverter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: bijie
 * @Date: 2024-01-08 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserVo implements Serializable {

    /**
     * 用户ID
     */

    @ExcelIgnore
    private String id;
    /**
     * 用户账号
     */
    @ExcelProperty("用户名称")
    private String username;
    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String nickname;
    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelIgnore
    private Integer status;
    /**
     * 创建时间
     */
    @ExcelIgnore
    private Timestamp createTime;
    @ExcelProperty(value = "性别",converter = SexConverter.class)

    private Integer sex;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("邮箱")
    private String email;
    /**
     * 更新时间
     */
    @ExcelIgnore
    private Timestamp updateTime;
    /**
     * 角色Id
     */
    @ExcelIgnore
    private String roleIds;
    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    private String roleNames;

    /**
     * 当前的岗位
     */
    @ExcelIgnore
    private String postIds;

    /**
     * 岗位名称
     */
    @ExcelProperty("岗位名称")
    private String postNames;


    /**
     * 所有的岗位信息
     */
    @ExcelIgnore
    private List<SystemPostVo> postList;

    /**
     * 部门ID
     */
    @ExcelIgnore
    private String deptIds;
    /**
     * 部门名称
     */
    @ExcelProperty("部门代码")
    private String deptNames;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;


    private List<String> deptIdList;

    private List<String> deptNameList;

    public String getCreateTime() {
        return DateUtil.formatDateToChinese(createTime);
    }

    public String getUpdateTime() {
        return  DateUtil.formatDateToChinese(updateTime);
    }


    @ExcelIgnore
    private String avatar;
}
