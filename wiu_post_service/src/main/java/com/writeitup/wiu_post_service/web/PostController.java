package com.writeitup.wiu_post_service.web;

import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(final @RequestBody CreatePostDTO post) {
        return new ResponseEntity<>(postService.create(post), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PostDTO> updatePost(final @RequestBody PostDTO post) {
        return new ResponseEntity<>(postService.update(post), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findPostById(final @PathVariable UUID id) {
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePostById(final @PathVariable UUID id) {
        postService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
