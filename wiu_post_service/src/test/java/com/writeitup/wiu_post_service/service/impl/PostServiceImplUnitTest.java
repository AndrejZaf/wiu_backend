package com.writeitup.wiu_post_service.service.impl;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.dto.ShortPostDTO;
import com.writeitup.wiu_post_service.exception.ForbiddenException;
import com.writeitup.wiu_post_service.exception.PostNotFoundException;
import com.writeitup.wiu_post_service.repository.PostRepository;
import com.writeitup.wiu_post_service.util.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplUnitTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostRepository postRepository;

    private Post newlyCreatedPost;
    private Post persistedPost;
    private PostDTO persistedPostDTO;
    private ShortPostDTO shortPersistedPostDTO;

    @BeforeEach
    void setup() {
        newlyCreatedPost = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .contentBlocks("Post Content")
                .tags(List.of("test", "title", "content"))
                .build();
        persistedPost = Post.builder()
                .id(UUID.randomUUID())
                .title(newlyCreatedPost.getTitle())
                .content(newlyCreatedPost.getContent())
                .contentBlocks(newlyCreatedPost.getContentBlocks())
                .tags(newlyCreatedPost.getTags())
                .build();
        persistedPostDTO = PostDTO.builder()
                .id(persistedPost.getId())
                .title(persistedPost.getTitle())
                .content(persistedPost.getContent())
                .contentBlocks(persistedPost.getContentBlocks())
                .tags(persistedPost.getTags())
                .build();
        shortPersistedPostDTO = ShortPostDTO.builder()
                .id(persistedPost.getId())
                .title(persistedPost.getTitle())
                .content(persistedPost.getContent())
                .build();
    }

    @Test
    void create_validParameters_returnsCreatedPost() {
        // arrange
        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .title(newlyCreatedPost.getTitle())
                .content(newlyCreatedPost.getContentBlocks())
                .tags(newlyCreatedPost.getTags())
                .build();
        when(postMapper.toPost(createPostDTO)).thenReturn(newlyCreatedPost);
        when(postRepository.save(newlyCreatedPost)).thenReturn(persistedPost);
        when(postMapper.toPostDTO(persistedPost)).thenReturn(persistedPostDTO);

        // act
        PostDTO actualResult = postService.create(createPostDTO);

        // assert
        assertThat(actualResult, is(persistedPostDTO));
    }

    @Test
    void update_invalidParameters_throwsNotFound() {
        // arrange
        // act
        // assert
        assertThrows(PostNotFoundException.class, () -> postService.update(persistedPostDTO));
    }

    @Test
    void update_invalidJwt_throwsForbidden() {
        // arrange
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("sub", UUID.randomUUID().toString()));
        createSecurityContext(jwt);
        persistedPost.setAuthorId(UUID.randomUUID());
        when(postRepository.findById(persistedPostDTO.getId())).thenReturn(Optional.ofNullable(persistedPost));

        // act
        // assert
        assertThrows(ForbiddenException.class, () -> postService.update(persistedPostDTO));
    }

    @Test
    void update_validParameters_returnsUpdatedPost() {
        // arrange
        persistedPost.setAuthorId(UUID.randomUUID());
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("sub", persistedPost.getAuthorId().toString()));
        createSecurityContext(jwt);
        when(postRepository.findById(persistedPostDTO.getId())).thenReturn(Optional.ofNullable(persistedPost));
        when(postRepository.save(persistedPost)).thenReturn(persistedPost);
        when(postMapper.toPostDTO(persistedPost)).thenReturn(persistedPostDTO);

        // act
        PostDTO actualResult = postService.update(persistedPostDTO);

        // assert
        assertThat(actualResult, notNullValue());
        assertThat(actualResult, is(persistedPostDTO));
    }

    @Test
    void findById_validParameters_returnsPost() {
        // arrange
        when(postRepository.findById(persistedPost.getId())).thenReturn(Optional.ofNullable(persistedPost));
        when(postMapper.toPostDTO(persistedPost)).thenReturn(persistedPostDTO);

        // act
        PostDTO actualResult = postService.findById(persistedPost.getId());

        // assert
        assertThat(actualResult, is(persistedPostDTO));
    }

    @Test
    void findById_invalidParameters_throwsNotFound() {
        // arrange
        when(postRepository.findById(persistedPost.getId())).thenReturn(Optional.empty());

        // act
        // assert
        assertThrows(PostNotFoundException.class, () -> postService.findById(persistedPost.getId()));
    }

    @Test
    void deleteById_invalidParameters_throwsNotFound() {
        // arrange
        when(postRepository.findById(persistedPost.getId())).thenReturn(Optional.empty());

        // act
        // assert
        assertThrows(PostNotFoundException.class, () -> postService.deleteById(persistedPost.getId()));
    }

    @Test
    void deleteById_validParameters_returnsVoid() {
        // arrange
        persistedPost.setAuthorId(UUID.randomUUID());
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaims()).thenReturn(Map.of("sub", persistedPost.getAuthorId().toString()));
        createSecurityContext(jwt);
        when(postRepository.findById(persistedPost.getId())).thenReturn(Optional.ofNullable(persistedPost));

        // act
        postService.deleteById(persistedPost.getId());

        // assert
        verify(postRepository).delete(persistedPost);
    }

    @Test
    void findAllBy_validParameters_returnsPageOfPosts() {
        // arrange
        int page = 0;
        int size = 10;
        String search = "test";
        String sort = "title;asc";
        PageImpl<Post> postPage = new PageImpl<>(List.of(persistedPost), PageRequest.of(page, size), 1);
        when(postRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(postPage);
        when(postMapper.toShortPostDTO(persistedPost)).thenReturn(shortPersistedPostDTO);

        // act
        Page<ShortPostDTO> actualResult = postService.findAllBy(search, page, size, sort);

        // assert
        assertThat(actualResult.getContent().get(0), is(shortPersistedPostDTO));
    }

    private static void createSecurityContext(Jwt jwt) {
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
