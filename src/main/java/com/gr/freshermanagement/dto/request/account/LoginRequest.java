package com.gr.freshermanagement.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
//    @Schema(description = "The user's username", example = "admin")
    @NotBlank(message = "username is mandatory")
    private String username;

//    @Schema(description = "The user's password", example = "admin")
    @NotBlank(message = "password is mandatory")
    private String password;

}
