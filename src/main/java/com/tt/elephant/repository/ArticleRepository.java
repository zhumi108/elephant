package com.tt.elephant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Query(value = "select * from article order by create_time desc",nativeQuery = true)
    Page<ArticleEntity> findByAllUsers(Pageable pageable);

    @Query(value = "select * from article as y where y.user_id=?1 order by create_time desc",nativeQuery = true)
    Page<ArticleEntity> findByUserContaining(String userId, Pageable pageable);

    @Query(value = "select * from article as y where y.article_id=?1",nativeQuery = true)
    ArticleEntity findByArticleId(String articleId);
}
