package com.writeitup.wiu_post_service.domain;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "post")
public class Post extends Auditable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Type(ListArrayType.class)
    @Column(name = "tags", columnDefinition = "varchar[]")
    private List<String> tags;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "search_vector", columnDefinition = "tsvector")
    private String searchVector;
}
