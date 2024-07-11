package com.writeitup.wiu_post_service.dto;

import com.writeitup.wiu_post_service.domain.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreatePostDTO {

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
