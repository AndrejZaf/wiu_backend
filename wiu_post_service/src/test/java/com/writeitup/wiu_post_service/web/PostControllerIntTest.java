package com.writeitup.wiu_post_service.web;

import com.writeitup.wiu_post_service.IntegrationBaseTest;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.writeitup.wiu_post_service.util.TestUtil.generateToken;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class PostControllerIntTest extends IntegrationBaseTest {

    private PostDTO updatePostInvalidOwner;
    private PostDTO updatePost;

    @BeforeEach
    void setup() {
        updatePostInvalidOwner = PostDTO.builder()
                .id(UUID.fromString("d05e777d-537d-4973-98fd-4504322c7d21"))
                .title("Test")
                .content("Test Content")
                .build();
        updatePost = PostDTO.builder()
                .id(UUID.fromString("d40abe07-2fc3-4905-839a-be4f0f0a1ae8"))
                .title("Modified Title")
                .content("Modified Content")
                .build();
    }

    @Test
    void deletePostById_invalidParameters_returnsNotFound() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(delete("/api/v1/posts/2f4c98c5-1413-4b9d-a616-151e4502c1e6")
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePostById_invalidParameters_returnsForbidden() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(delete("/api/v1/posts/d05e777d-537d-4973-98fd-4504322c7d21")
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deletePostById_validParameters_returnsNoContent() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(delete("/api/v1/posts/a8bf75cc-5d78-4f42-9387-0dd3be6bc454")
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isNoContent());
    }

    @Test
    void findById_validParameters_returnsPostDTO() throws Exception {
        // arrange
        PostDTO postDTO = PostDTO.builder()
                .id(UUID.fromString("f74ebd6f-eba9-4dca-9859-72ed8acedd97"))
                .title("Test Post Title 1")
                .content("Test Post Content 1")
                .build();

        // act
        MvcResult actualResult = mockMvc.perform(get("/api/v1/posts/f74ebd6f-eba9-4dca-9859-72ed8acedd97"))
                .andExpect(status().isOk()).andReturn();

        // assert
        PostDTO actualPost = objectMapper.readValue(actualResult.getResponse().getContentAsString(), PostDTO.class);
        assertThat(actualPost, is(postDTO));
    }

    @Test
    void findById_invalidParameters_returnsNotFound() throws Exception {
        // arrange
        // act
        // assert
        mockMvc.perform(get("/api/v1/posts/1a9b6303-656c-4e88-9ec6-cb31fe34c7f2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPost_invalidParameters_returnsBadRequest() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");
        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .title("Test")
                .build();

        // act
        // assert
        mockMvc.perform(post("/api/v1/posts")
                        .content(objectMapper.writeValueAsString(createPostDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPost_validParameters_returnsCreatedPost() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");
        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .title("Test")
                .content("Test Content")
                .build();
        PostDTO postDTO = PostDTO.builder()
                .title("Test")
                .content("Test Content")
                .build();

        // act
        MockHttpServletResponse actualResponse = mockMvc.perform(post("/api/v1/posts")
                        .content(objectMapper.writeValueAsString(createPostDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isCreated()).andReturn().getResponse();
        PostDTO actualPost = objectMapper.readValue(actualResponse.getContentAsString(), PostDTO.class);

        // assert
        assertThat(actualPost.getTitle(), is(postDTO.getTitle()));
        assertThat(actualPost.getContent(), is(postDTO.getContent()));
    }

    @Test
    void updatePost_invalidParameters_returnsNotFound() throws Exception {
        // arrange
        updatePostInvalidOwner.setId(UUID.fromString("2f4c98c5-1413-4b9d-a616-151e4502c1e6"));
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePostInvalidOwner))
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePost_invalidParameters_returnsForbidden() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePostInvalidOwner))
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updatePost_invalidParameters_returnsBadRequest() throws Exception {
        // arrange
        PostDTO invalidPost = PostDTO.builder()
                .title("Invalid title")
                .build();
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        // assert
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidPost))
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePost_validParameters_returnsUpdatedPost() throws Exception {
        // arrange
        String jwtToken = generateToken("1a9b6303-656c-4e88-9ec6-cb31fe34c7f2", "USER");

        // act
        MockHttpServletResponse actualResult = mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePost))
                        .header("Authorization", String.format("Bearer %s", jwtToken)))
                .andExpect(status().isOk()).andReturn().getResponse();
        PostDTO actualPost = objectMapper.readValue(actualResult.getContentAsString(), PostDTO.class);

        // assert
        assertThat(actualPost, is(updatePost));
    }

    @Test
    void getPosts_validParameters_returnsPageOfPosts() throws Exception {
        // arrange
        int page = 0;
        int size = 10;
        String search = "read1";

        // act
        MockHttpServletResponse actualResponse = mockMvc.perform(get(String.format("/api/v1/posts?page=%d&size=%d&search=%s", page, size, search)))
                .andExpect(status().isOk()).andReturn().getResponse();
        String content = actualResponse.getContentAsString();

        // assert
        assertThat(content, containsString("\"numberOfElements\":1"));
        assertThat(content, containsString("\"content\":[{\"id\":\"f74ebd6f-eba9-4dca-9859-72ed8acedd97\",\"title\":\"Test Post Title 1\",\"content\":\"Test Post Content 1\",\"tags\":null}]"));
    }
}
