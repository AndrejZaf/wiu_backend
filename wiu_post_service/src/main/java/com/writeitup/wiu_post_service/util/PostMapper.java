package com.writeitup.wiu_post_service.util;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    PostDTO toPostDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePost(PostDTO postDTO, @MappingTarget Post post);

    Post toPost(CreatePostDTO post);
}
