package com.searchengine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    private String website;

    private String description;

    private String headings;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer wordCount;

    private LocalDateTime indexedAt;

    /*
     * PostgreSQL generates this value using the trigger.
     * Hibernate must not INSERT or UPDATE this column.
     */
    @Column(
            name = "search_vector",
            insertable = false,
            updatable = false
    )
    private String searchVector;


    // =========================
    // GETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getWebsite() {
        return website;
    }

    public String getDescription() {
        return description;
    }

    public String getHeadings() {
        return headings;
    }

    public String getContent() {
        return content;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public LocalDateTime getIndexedAt() {
        return indexedAt;
    }

    public String getSearchVector() {
        return searchVector;
    }


    // =========================
    // SETTERS
    // =========================

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHeadings(String headings) {
        this.headings = headings;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public void setIndexedAt(LocalDateTime indexedAt) {
        this.indexedAt = indexedAt;
    }

    public void setSearchVector(String searchVector) {
        this.searchVector = searchVector;
    }
}