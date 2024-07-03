package com.writeitup.wiu_post_service.web;

import com.writeitup.wiu_post_service.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostControllerUnitTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;


    @Test
    void createPost_returns200() {
        // arrange

        // act

        // assert
    }
}
