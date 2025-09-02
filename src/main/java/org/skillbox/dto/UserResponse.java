package org.skillbox.dto;

import org.skillbox.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
}