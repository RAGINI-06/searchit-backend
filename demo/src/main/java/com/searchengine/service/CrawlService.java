package com.searchengine.service;

import com.searchengine.config.WebsiteList;
import com.searchengine.crawler.CrawlTask;
import com.searchengine.crawler.JsoupCrawler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CrawlService {

    private final JsoupCrawler crawler;

    // Number of websites to crawl simultaneously
    private static final int THREAD_COUNT = 5;

    public CrawlService(JsoupCrawler crawler) {
        this.crawler = crawler;
    }

    public void crawlAllWebsites() {

        ExecutorService executorService =
                Executors.newFixedThreadPool(THREAD_COUNT);

        for (String website : WebsiteList.WEBSITES) {

            executorService.submit(new CrawlTask(crawler, website));

        }

        executorService.shutdown();

        try {

            if (!executorService.awaitTermination(24, TimeUnit.HOURS)) {

                executorService.shutdownNow();

            }

        } catch (InterruptedException e) {

            executorService.shutdownNow();
            Thread.currentThread().interrupt();

        }

        System.out.println("=================================");
        System.out.println("All Websites Crawled Successfully");
        System.out.println("=================================");

    }

}