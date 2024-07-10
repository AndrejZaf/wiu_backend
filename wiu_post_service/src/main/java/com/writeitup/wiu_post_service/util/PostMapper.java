package com.writeitup.wiu_post_service.util;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "byteArrayToString")
    @Mapping(source = "content", target = "content", qualifiedByName = "cleanseHtml")
    PostDTO toPostDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "stringToByteArray")
    @Mapping(source = "content", target = "content", qualifiedByName = "cleanseHtml")
    void updatePost(PostDTO postDTO, @MappingTarget Post post);

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "stringToByteArray")
    @Mapping(source = "content", target = "content", qualifiedByName = "cleanseHtml")
    Post toPost(CreatePostDTO post);

    @Named("stringToByteArray")
    static byte[] stringToByteArray(String imageData) {
        return imageData.getBytes();
    }

    @Named("byteArrayToString")
    static String byteArrayToString(byte[] imageData) {
        return new String(imageData);
    }

    @Named("cleanseHtml")
    static String cleanseHtml(String content) {
        Source htmlSource = new Source(content);
        Segment segment = new Segment(htmlSource, 0, htmlSource.length());
        Renderer htmlRender = new Renderer(segment).setIncludeHyperlinkURLs(true);
        return htmlRender.toString();
    }
}
