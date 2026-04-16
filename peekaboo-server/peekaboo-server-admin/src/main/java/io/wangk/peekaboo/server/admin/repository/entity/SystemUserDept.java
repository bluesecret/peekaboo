package io.wangk.peekaboo.server.admin.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 部门表(SystemUserDept)实体类
 * @since 2024-01-09 09:08:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("system_user_dept")
public class SystemUserDept implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String deptId;

    private String status;


}

