package com.writeitup.wiu_post_service.web;

import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.dto.ShortPostDTO;
import com.writeitup.wiu_post_service.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerUnitTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private PostDTO postDTO;
    private ShortPostDTO shortPostDTO;

    @BeforeEach
    void setup() {
        postDTO = PostDTO.builder()
                .id(UUID.randomUUID())
                .title("Post Title")
                .content("Post Content")
                .tags(List.of("test", "title", "content"))
                .build();
        shortPostDTO = ShortPostDTO.builder()
                .id(UUID.randomUUID())
                .title("Post Title")
                .content("Post Content")
                .build();
    }

    @Test
    void createPost_validParameters_returns200() {
        // arrange
        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .tags(postDTO.getTags())
                .build();
        when(postService.create(createPostDTO)).thenReturn(postDTO);

        // act
        ResponseEntity<PostDTO> createPostResponse = postController.createPost(createPostDTO);

        // assert
        assertThat(createPostResponse.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(createPostResponse.getBody(), is(postDTO));
    }

    @Test
    void deletePost_validParameters_returns200() {
        // arrange
        // act
        postController.deletePostById(postDTO.getId());

        // assert
        verify(postService).deleteById(postDTO.getId());
    }

    @Test
    void updatePost_validParameters_returns200() {
        // arrange
        when(postService.update(postDTO)).thenReturn(postDTO);

        // act
        ResponseEntity<PostDTO> updatePostResponse = postController.updatePost(postDTO);

        // assert
        assertThat(updatePostResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(updatePostResponse.getBody(), is(postDTO));
    }

    @Test
    void getPostById_validParameters_returns200() {
        // arrange
        when(postService.findById(postDTO.getId())).thenReturn(postDTO);

        // act
        ResponseEntity<PostDTO> getPostByIdResponse = postController.getPostById(postDTO.getId());

        // assert
        assertThat(getPostByIdResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getPostByIdResponse.getBody(), is(postDTO));
    }

    @Test
    void getPosts_validParameters_returns200() {
        // arrange
        int page = 0;
        int size = 10;
        String search = "test";
        String sort = "title;asc";
        PageImpl<ShortPostDTO> postPage = new PageImpl<>(List.of(shortPostDTO), PageRequest.of(page, size), 1);
        when(postService.findAllBy(search, page, size, sort)).thenReturn(postPage);

        // act
        ResponseEntity<Page<ShortPostDTO>> postsResponse = postController.getPosts(page, size, search, sort);

        // assert
        assertThat(postsResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(postsResponse.getBody(), is(postPage));
    }
}
