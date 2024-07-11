package com.writeitup.wiu_post_service.util;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.dto.CreatePostDTO;
import com.writeitup.wiu_post_service.dto.PostDTO;
import com.writeitup.wiu_post_service.dto.ShortPostDTO;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "byteArrayToString")
    @Mapping(source = "content", target = "readTime", qualifiedByName = "readTimeCalculation")
    PostDTO toPostDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "stringToByteArray")
    @Mapping(source = "content", target = "content", qualifiedByName = "cleanseHtml")
    void updatePost(PostDTO postDTO, @MappingTarget Post post);

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "stringToByteArray")
    @Mapping(source = "content", target = "content", qualifiedByName = "cleanseHtml")
    Post toPost(CreatePostDTO post);

    @Named("stringToByteArray")
    static byte[] stringToByteArray(final String imageData) {
        return imageData.getBytes();
    }

    @Named("byteArrayToString")
    static String byteArrayToString(final byte[] imageData) {
        return new String(imageData);
    }

    @Mapping(source = "imageData", target = "imageData", qualifiedByName = "byteArrayToString")
    @Mapping(source = "content", target = "content", qualifiedByName = "shortenContent")
    ShortPostDTO toShortPostDTO(Post post);

    @Named("cleanseHtml")
    static String cleanseHtml(final String content) {
        final Source htmlSource = new Source(content);
        final Segment segment = new Segment(htmlSource, 0, htmlSource.length());
        final Renderer htmlRender = new Renderer(segment).setIncludeHyperlinkURLs(true);
        return htmlRender.toString();
    }

    @Named("readTimeCalculation")
    static int calculateReadTime(final String content) {
        if (isNull(content) || content.isEmpty()) {
            return 0;
        }

        final String[] words = content.trim().split("\\s+");
        return (int) Math.ceil((double) words.length / 200);
    }

    @Named("shortenContent")
    static String shortenContent(final String content) {
        return StringUtils.left(content, 300);
    }
}
