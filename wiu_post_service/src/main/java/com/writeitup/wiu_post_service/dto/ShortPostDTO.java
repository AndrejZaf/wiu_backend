package com.writeitup.wiu_post_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ShortPostDTO {

    @NotNull
    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String imageData;
}
