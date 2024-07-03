package com.writeitup.wiu_post_service.service;

import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;

import java.util.UUID;

public interface PostService {
    PostDTO create(CreatePostDTO post);

    PostDTO update(PostDTO post);

    PostDTO findById(UUID id);

    void deleteById(UUID id);
}
