package com.writeitup.wiu_post_service.specification;

import com.writeitup.wiu_post_service.domain.Post;
import com.writeitup.wiu_post_service.domain.Post_;
import com.writeitup.wiu_post_service.domain.Status;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@UtilityClass
public class PostSpecification {

    private static final String TSVECTOR_MATCH = "tsvector_match";
    private static final String PLAIN_TO_TSQUERY = "plainto_tsquery";

    public static Specification<Post> createPostSpecification(final String search, final String authorId, Status status) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (nonNull(search) && !isEmpty(search)) {
                Predicate searchPredicate = criteriaBuilder.isTrue(criteriaBuilder.function(TSVECTOR_MATCH, Boolean.class,
                        root.get(Post_.searchVector),
                        criteriaBuilder.function(PLAIN_TO_TSQUERY, String.class, criteriaBuilder.literal(search))));
                predicates.add(searchPredicate);
            }

            if (nonNull(authorId) && isEmpty(authorId)) {
                Predicate authorPredicate = criteriaBuilder.equal(root.get(Post_.authorId), authorId);
                predicates.add(authorPredicate);
            }

            Predicate statusPredicate = criteriaBuilder.equal(root.get(Post_.status), status);
            predicates.add(statusPredicate);

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
