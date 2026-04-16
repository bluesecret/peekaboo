package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

import java.util.List;

/**
 * @author bijie
 * @since 2024/3/22
 */

@Data
public class SystemRoleMenuDTO {

     private String id;

     private String parentId;

     private String component;

     private String path;

     private String componentName;

     private String permission;

     private MeteInfo meteInfo;

     private List<SystemRoleMenuDTO> children ;





}
