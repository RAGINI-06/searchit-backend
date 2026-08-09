package com.searchengine.repository;

import com.searchengine.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository
        extends JpaRepository<SearchHistory,Long> {
}