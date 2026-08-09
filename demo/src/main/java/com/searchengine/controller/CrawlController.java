package com.searchengine.controller;

import com.searchengine.service.CrawlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crawler")
public class CrawlController {

    private final CrawlService crawlService;

    public CrawlController(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startCrawler() {

        crawlService.crawlAllWebsites();

        return ResponseEntity.ok("Crawling Completed Successfully.");

    }

}