package com.writeitup.wiu_user_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID id;
    private String email;
    private String username;
    private String bio;
    private byte[] image;
}
