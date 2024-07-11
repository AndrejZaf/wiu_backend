package com.writeitup.wiu_post_service.service.impl;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.domain.Status;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.exception.ForbiddenException;
import com.writeitup.wiu_post_service.exception.PostNotFoundException;
import com.writeitup.wiu_post_service.repository.PostRepository;
import com.writeitup.wiu_post_service.service.PostService;
import com.writeitup.wiu_post_service.util.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.writeitup.wiu_post_service.specification.PostSpecification.createPostSpecification;
import static com.writeitup.wiu_post_service.util.JwtUtil.getJwtClaim;
import static com.writeitup.wiu_post_service.util.SortUtil.parseSortParams;
import static com.writeitup.wiu_post_service.util.VectorUtil.generateTsVector;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostDTO create(final CreatePostDTO createPostDTO) {
        log.debug("Creating a new post with title=[{}]", createPostDTO.getTitle());
        Post post = postMapper.toPost(createPostDTO);
        final String vector = generateTsVector(post.getTitle(), createPostDTO.getContent(), post.getTags());
        post.setSearchVector(vector);
        post = postRepository.save(post);
        log.debug("Successfully created a post with title=[{}]", createPostDTO.getTitle());
        return postMapper.toPostDTO(post);
    }

    @Override
    public PostDTO update(final PostDTO postDTO) {
        log.debug("Updating a post with ID=[{}]", postDTO.getId());
        Post post = postRepository.findById(postDTO.getId()).orElseThrow(PostNotFoundException::new);
        validateOwnership(post.getAuthorId(), UUID.fromString(getJwtClaim("sub")));
        postMapper.updatePost(postDTO, post);
        final String vector = generateTsVector(post.getTitle(), postDTO.getContent(), post.getTags());
        post.setSearchVector(vector);
        post = postRepository.save(post);
        log.debug("Successfully updated a post with ID=[{}]", postDTO.getId());
        return postMapper.toPostDTO(post);
    }

    @Override
    public PostDTO findById(final UUID id) {
        log.debug("Retrieving a post with ID=[{}]", id);
        final Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        log.debug("Successfully retrieved a post with ID=[{}]", id);
        return postMapper.toPostDTO(post);
    }

    @Override
    public void deleteById(final UUID id) {
        log.debug("Deleting a post with ID=[{}]", id);
        final Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        validateOwnership(post.getAuthorId(), UUID.fromString(getJwtClaim("sub")));
        postRepository.delete(post);
        log.debug("Successfully deleted a post with ID=[{}]", id);
    }

    @Override
    public Page<PostDTO> findAllBy(final String search, final int page, final int size, final String sort) {
        log.debug("Retrieving a page of post by page=[{}], size=[{}], search=[{}], sort=[{}]", page, size, search, sort);
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(parseSortParams(sort)));
        final Specification<Post> postSpecification = createPostSpecification(search, null, Status.PUBLISHED);
        return postRepository.findAll(postSpecification, pageRequest)
                .map(postMapper::toPostDTO);
    }

    @Override
    public Page<PostDTO> findAllByLoggedInUser(final String search, final int page, final int size, final String sort, final Status status) {
        final String authorId = getJwtClaim("sub");
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(parseSortParams(sort)));
        final Specification<Post> postSpecification = createPostSpecification(search, authorId, status);
        return postRepository.findAll(postSpecification, pageRequest)
                .map(postMapper::toPostDTO);
    }

    private void validateOwnership(final UUID postAuthorId, final UUID tokenAuthorId) {
        if (!postAuthorId.equals(tokenAuthorId)) {
            log.warn("The author with ID [{}] does not have permissions to modify the post which belongs to author ID [{}]",
                    tokenAuthorId, postAuthorId);
            throw new ForbiddenException();
        }
    }
}
