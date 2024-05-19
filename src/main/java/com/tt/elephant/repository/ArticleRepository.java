package com.tt.elephant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query(value = "select * from article where userId=:userId ",nativeQuery = true)
    Page<ArticleEntity> findByUserContaining(@Param("userId") String userId, Pageable pageable);
}
