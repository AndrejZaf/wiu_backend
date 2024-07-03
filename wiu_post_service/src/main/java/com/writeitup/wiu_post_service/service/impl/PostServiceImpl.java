package com.writeitup.wiu_post_service.service.impl;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.repository.PostRepository;
import com.writeitup.wiu_post_service.service.PostService;
import com.writeitup.wiu_post_service.util.PostMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostDTO create(final CreatePostDTO createPostDTO) {
        Post post = postMapper.toPost(createPostDTO);
        post = postRepository.save(post);
        return postMapper.toPostDTO(post);
    }

    @Override
    public PostDTO update(final PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getId()).orElseThrow(EntityNotFoundException::new);
        postMapper.updatePost(postDTO, post);
        post = postRepository.save(post);
        return postMapper.toPostDTO(post);
    }

    @Override
    public PostDTO findById(final UUID id) {
        final Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return postMapper.toPostDTO(post);
    }

    @Override
    public void deleteById(final UUID id) {
        postRepository.deleteById(id);
    }
}
