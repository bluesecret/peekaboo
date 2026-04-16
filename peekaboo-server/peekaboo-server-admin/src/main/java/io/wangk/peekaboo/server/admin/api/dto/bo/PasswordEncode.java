package io.wangk.peekaboo.server.admin.api.dto.bo;

import lombok.Data;

/**
 * @author bijie
 * @since 2024/4/16
 */
@Data
public class PasswordEncode {
    private String oldPassword;
    private String newPassword;
    private String newPassword_confirmation;
}
