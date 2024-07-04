package com.writeitup.wiu_post_service.service;

import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PostService {

    PostDTO create(CreatePostDTO post);

    PostDTO update(PostDTO post);

    PostDTO findById(UUID id);

    void deleteById(UUID id);

    Page<PostDTO> findAllBy(String search, int page, int size, String sort);
}
