package com.searchengine.repository;

import com.searchengine.entity.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    boolean existsByUrl(String url);

    @Query(value = """
            SELECT *
            FROM pages
            WHERE search_vector @@ plainto_tsquery(:keyword)
            ORDER BY ts_rank(search_vector, plainto_tsquery(:keyword)) DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM pages
            WHERE search_vector @@ plainto_tsquery(:keyword)
            """,
            nativeQuery = true)
    org.springframework.data.domain.Page<Page> search(
            String keyword,
            Pageable pageable
    );

    @Query(value = """
SELECT DISTINCT title
FROM pages
WHERE LOWER(title) LIKE LOWER(CONCAT(:query,'%'))
   OR LOWER(description) LIKE LOWER(CONCAT(:query,'%'))
   OR LOWER(headings) LIKE LOWER(CONCAT(:query,'%'))
ORDER BY title
LIMIT 10
""", nativeQuery = true)
    List<String> findSuggestions(String query);
}