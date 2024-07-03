package com.writeitup.wiu_post_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PostDTO {

    private UUID id;
    private String title;
    private String content;
}
