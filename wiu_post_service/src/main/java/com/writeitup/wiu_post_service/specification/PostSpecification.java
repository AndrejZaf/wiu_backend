package com.writeitup.wiu_post_service.specification;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.domain.Post_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@UtilityClass
public class PostSpecification {

    private static final String TSVECTOR_MATCH = "tsvector_match";
    private static final String PLAIN_TO_TSQUERY = "plainto_tsquery";

    public static Specification<Post> createSearchSpecification(final String search) {
        return (root, query, criteriaBuilder) -> {
            if (isEmpty(search)) {
                return null;
            }

            return criteriaBuilder.isTrue(criteriaBuilder.function(TSVECTOR_MATCH, Boolean.class,
                    root.get(Post_.searchVector),
                    criteriaBuilder.function(PLAIN_TO_TSQUERY, String.class, criteriaBuilder.literal(search))));
        };
    }
}
