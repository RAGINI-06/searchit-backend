package com.searchengine.crawler;

public class CrawlTask implements Runnable {

    private final JsoupCrawler crawler;
    private final String website;

    public CrawlTask(JsoupCrawler crawler, String website) {
        this.crawler = crawler;
        this.website = website;
    }

    @Override
    public void run() {

        System.out.println("--------------------------------");
        System.out.println("Starting Crawl : " + website);
        System.out.println("--------------------------------");

        crawler.crawl(website);

        System.out.println("--------------------------------");
        System.out.println("Finished Crawl : " + website);
        System.out.println("--------------------------------");

    }
}