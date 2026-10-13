package dev.danvega.repository;

import dev.danvega.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Article> findByAuthorOrderByCreatedAtDesc(String author, Pageable pageable);
    Article findBySlug(String slug);

    @Query("SELECT a FROM Article a WHERE " +
           "(:search IS NULL OR :search = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.content) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:published IS NULL OR a.published = :published)")
    Page<Article> searchArticles(@Param("search") String search,
                                  @Param("published") Boolean published,
                                  Pageable pageable);
}
