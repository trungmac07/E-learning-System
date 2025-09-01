package com.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String keycloakId;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Boolean gender;
}