package com.searchengine.service;

import com.searchengine.dto.SearchResponse;
import com.searchengine.entity.Page;
import com.searchengine.entity.SearchHistory;
import com.searchengine.repository.PageRepository;
import com.searchengine.repository.SearchHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final PageRepository repository;
    private final SearchHistoryRepository historyRepository;

    public SearchService(PageRepository repository,
                         SearchHistoryRepository historyRepository) {

        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    public List<SearchResponse> search(String keyword,
                                       int page,
                                       int size) {

        // Save search history
        SearchHistory history = new SearchHistory();
        history.setKeyword(keyword);
        history.setSearchedAt(LocalDateTime.now());
        historyRepository.save(history);

        Pageable pageable = PageRequest.of(page, size);

        org.springframework.data.domain.Page<Page> pages =
                repository.search(keyword, pageable);

        List<SearchResponse> results = new ArrayList<>();

        for (Page p : pages.getContent()) {

            SearchResponse response = new SearchResponse(
                    p.getTitle(),
                    p.getUrl(),
                    createSnippet(p.getContent(), keyword)
            );

            results.add(response);
        }

        return results;
    }

    private String createSnippet(String text, String keyword) {

        if (text == null || text.isEmpty()) {
            return "";
        }

        int index = text.toLowerCase().indexOf(keyword.toLowerCase());

        if (index == -1) {
            return text.substring(0, Math.min(150, text.length())) + "...";
        }

        int start = Math.max(0, index - 50);
        int end = Math.min(text.length(), index + 100);

        return text.substring(start, end) + "...";
    }
    public List<String> getSuggestions(String query) {

        return repository.findSuggestions(query);

    }
}