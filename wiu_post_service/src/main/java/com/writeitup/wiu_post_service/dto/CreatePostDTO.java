package com.writeitup.wiu_post_service.dto;

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
    private List<String> tags;
}
