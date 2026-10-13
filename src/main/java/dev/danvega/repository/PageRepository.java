package dev.danvega.repository;

import dev.danvega.model.StaticPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<StaticPage, Long> {
    Page<StaticPage> findByPublishedTrueOrderByTitleAsc(Pageable pageable);
    Optional<StaticPage> findBySlugAndPublishedTrue(String slug);

    @Query("SELECT p FROM StaticPage p WHERE " +
           "(:search IS NULL OR :search = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:published IS NULL OR p.published = :published)")
    Page<StaticPage> searchPages(@Param("search") String search,
                                  @Param("published") Boolean published,
                                  Pageable pageable);
}
