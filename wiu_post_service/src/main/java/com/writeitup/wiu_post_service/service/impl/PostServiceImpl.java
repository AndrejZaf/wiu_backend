package com.writeitup.wiu_post_service.service.impl;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.exception.ForbiddenException;
import com.writeitup.wiu_post_service.repository.PostRepository;
import com.writeitup.wiu_post_service.service.PostService;
import com.writeitup.wiu_post_service.util.PostMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.writeitup.wiu_post_service.specification.PostSpecification.createSearchSpecification;
import static com.writeitup.wiu_post_service.util.JwtUtil.getJwtClaim;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostDTO create(final CreatePostDTO createPostDTO) {
        Post post = postMapper.toPost(createPostDTO);
        final String vector = generateTsVector(createPostDTO.getTitle(), createPostDTO.getContent(), Collections.emptyList());
        post.setSearchVector(vector);
        post = postRepository.save(post);
        return postMapper.toPostDTO(post);
    }

    @Override
    public PostDTO update(final PostDTO postDTO) {
        Post post = postRepository.findById(postDTO.getId()).orElseThrow(EntityNotFoundException::new);
        validateOwnership(post.getAuthorId(), UUID.fromString(getJwtClaim("id")));
        postMapper.updatePost(postDTO, post);
        final String vector = generateTsVector(postDTO.getTitle(), postDTO.getContent(), Collections.emptyList());
        post.setSearchVector(vector);
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
        final Post post = postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        validateOwnership(post.getAuthorId(), UUID.fromString(getJwtClaim("id")));
        postRepository.delete(post);
    }

    @Override
    public Page<PostDTO> findAllBy(final String search, final int page, final int size, final String sort) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(parseSortParams(sort)));
        final Specification<Post> postSpecification = createSearchSpecification(search);
        return postRepository.findAll(postSpecification, pageRequest)
                .map(postMapper::toPostDTO);
    }

    private Sort.Order parseSortParams(final String sortParams) {
        final String[] split = sortParams.split(";");
        return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
    }

    private String generateTsVector(final String title, final String content, final List<String> tags) {
        return String.format("%s %s %s", title, content, String.join(" ", tags));
    }

    private void validateOwnership(final UUID postAuthorId, final UUID tokenAuthorId) {
        if (!postAuthorId.equals(tokenAuthorId)) {
            throw new ForbiddenException();
        }
    }
}
