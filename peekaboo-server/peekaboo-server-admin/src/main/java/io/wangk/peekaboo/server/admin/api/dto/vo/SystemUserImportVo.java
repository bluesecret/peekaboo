package io.wangk.peekaboo.server.admin.api.dto.vo;
import cn.ruixi.azure.rainbow.boot.common.validation.Telephone;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author bijie
 * @since 2024/4/12
 */
public class SystemUserImportVo {

    /**
     * 用户账号
     */
    @ExcelProperty("用户名称")
    @NotEmpty(message = "用户名称不能为空")
    private String username;
    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String nickname;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("手机号")
    @Telephone
    private String mobile;

    @ExcelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    private String roleNames;
    /**
     * 岗位名称
     */
    @ExcelProperty("岗位名称")
    private String postNames;
    /**
     * 部门名称
     */
    @ExcelProperty("部门名称")
    private String deptNames;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getPostNames() {
        return postNames;
    }

    public void setPostNames(String postNames) {
        this.postNames = postNames;
    }

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
