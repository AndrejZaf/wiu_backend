package com.writeitup.wiu_post_service.web;

import com.writeitup.wiu_post_service.domain.Status;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve posts by page, size, search and sort")
    @ApiResponse(responseCode = "200", description = "Returns a page of posts",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))})
    public ResponseEntity<Page<PostDTO>> getPosts(@RequestParam final int page, @RequestParam final int size,
                                                  @RequestParam(required = false) final String search,
                                                  @RequestParam(defaultValue = "title;desc") String sort) {
        log.info("Received request to retrieve a page of posts by: page=[{}], size=[{}], search=[{}], sort=[{}]", page, size, search, sort);
        return new ResponseEntity<>(postService.findAllBy(search, page, size, sort), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The parameters supplied for the creation of the post are invalid",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid final CreatePostDTO post) {
        log.info("Received request to create post with the following title=[{}]", post.getTitle());
        return new ResponseEntity<>(postService.create(post), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class))}),
            @ApiResponse(responseCode = "400", description = "The parameters supplied for the update of the post are invalid",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "The user must be logged in to update this post",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "The user does not have permissions to update this post",
                    content = {@Content(mediaType = "application/json", schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "The post with the provided ID is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    public ResponseEntity<PostDTO> updatePost(@RequestBody @Valid final PostDTO post) {
        log.info("Received request to update post with ID=[{}]", post.getId());
        return new ResponseEntity<>(postService.update(post), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Get post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a post",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class))}),
            @ApiResponse(responseCode = "404", description = "The post with the provided ID is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema())})})
    public ResponseEntity<PostDTO> getPostById(@PathVariable final UUID id) {
        log.info("Received request to retrieve post with ID=[{}]", id);
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post successfully deleted"),
            @ApiResponse(responseCode = "401", description = "The user must be logged in to delete this post"),
            @ApiResponse(responseCode = "403", description = "The user does not have permissions to delete this post"),
            @ApiResponse(responseCode = "404", description = "The post with the provided ID is not found")})
    public ResponseEntity<Void> deletePostById(@PathVariable final UUID id) {
        log.info("Received request to delete post with ID=[{}]", id);
        postService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Retrieve posts by page, size, search and sort")
    @ApiResponse(responseCode = "200", description = "Returns a page of posts",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))})
    public ResponseEntity<Page<PostDTO>> getPostsForLoggedInUser(@RequestParam final int page, @RequestParam final int size,
                                                                 @RequestParam(required = false) final String search,
                                                                 @RequestParam(defaultValue = "title;desc") String sort,
                                                                 @RequestParam Status status) {
        log.info("Received request to retrieve a page of posts for authenticated user by: page=[{}], size=[{}], search=[{}], sort=[{}], status=[{}]", page, size, search, sort, status);
        return new ResponseEntity<>(postService.findAllByLoggedInUser(search, page, size, sort, status), HttpStatus.OK);
    }
}
