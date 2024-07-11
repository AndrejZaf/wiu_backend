package com.writeitup.wiu_post_service.dto;

import com.writeitup.wiu_post_service.domain.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostDTO {

    @NotNull
    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String contentBlocks;
    @NotNull
    private Status status;
    @NotNull
    private String imageData;
    private List<String> tags;
}
