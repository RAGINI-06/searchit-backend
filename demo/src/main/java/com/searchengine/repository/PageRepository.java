package com.searchengine.repository;

import com.searchengine.entity.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    boolean existsByUrl(String url);

    @Query(value = """
        SELECT *,
            (
                CASE
                    WHEN LOWER(title) = LOWER(:keyword) THEN 100
                    WHEN LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 50
                    WHEN LOWER(headings) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 30
                    WHEN LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 15
                    ELSE 0
                END
                +
                COALESCE(
                    ts_rank(
                        search_vector,
                        websearch_to_tsquery('english', :keyword)
                    ) * 10,
                    0
                )
            ) AS relevance_score
        FROM pages
        WHERE
            search_vector @@ websearch_to_tsquery('english', :keyword)
            OR LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(headings) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(content) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY relevance_score DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM pages
        WHERE
            search_vector @@ websearch_to_tsquery('english', :keyword)
            OR LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(headings) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(content) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """,
            nativeQuery = true)
    org.springframework.data.domain.Page<Page> search(
            String keyword,
            Pageable pageable
    );

    @Query(value = """
            SELECT DISTINCT title
            FROM pages
            WHERE LOWER(title) LIKE LOWER(CONCAT(:query, '%'))
               OR LOWER(description) LIKE LOWER(CONCAT(:query, '%'))
               OR LOWER(headings) LIKE LOWER(CONCAT(:query, '%'))
            ORDER BY title
            LIMIT 10
            """, nativeQuery = true)
    List<String> findSuggestions(String query);
}