package com.writeitup.wiu_post_service.repository;

import com.writeitup.wiu_post_service.domain.Post;
import org.postgresql.util.PGobject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    @Query(value = "SELECT to_tsvector(:text)", nativeQuery = true)
    PGobject generateVector(String text);
}
