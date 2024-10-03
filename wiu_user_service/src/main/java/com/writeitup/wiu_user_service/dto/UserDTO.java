package com.writeitup.wiu_user_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {

    @NotNull
    private UUID id;
    @NotNull
    private String email;
    private String username;
    private String bio;
    private String imageData;
}
