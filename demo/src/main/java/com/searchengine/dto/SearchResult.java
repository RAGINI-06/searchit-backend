package com.searchengine.dto;

public class SearchResult {

    private String title;
    private String url;
    private String snippet;
    private double score;

    public SearchResult() {}

    public SearchResult(String title, String url, String snippet, double score) {
        this.title = title;
        this.url = url;
        this.snippet = snippet;
        this.score = score;
    }

    // Getters and Setters
}