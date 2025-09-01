package com.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateTeacherRequest extends UpdateUserRequest {
    
    @Size(max = 50, message = "Department name must not exceed 50 characters")
    private String department;
}