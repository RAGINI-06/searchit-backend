package com.searchengine.crawler;

import com.searchengine.entity.Page;
import com.searchengine.repository.PageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@Component
public class JsoupCrawler {

    private static final int MAX_PAGES_PER_SITE = 1000;

    private final PageRepository pageRepository;

    public JsoupCrawler(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public void crawl(String startUrl) {

        // Each website gets its own visited set
        Set<String> visited = new HashSet<>();

        Queue<String> queue = new LinkedList<>();

        queue.add(startUrl);

        String host;

        try {
            host = new URL(startUrl).getHost();
        } catch (Exception e) {
            System.out.println("Invalid URL: " + startUrl);
            return;
        }

        int pages = 0;

        System.out.println();
        System.out.println("======================================");
        System.out.println("Starting crawl: " + startUrl);
        System.out.println("======================================");

        while (!queue.isEmpty() && pages < MAX_PAGES_PER_SITE) {

            String url = queue.poll();

            if (visited.contains(url)) {
                continue;
            }

            visited.add(url);

            try {

                System.out.println("Crawling: " + url);

                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .followRedirects(true)
                        .get();

                System.out.println("Fetched: " + document.title());

                // Check database
                if (!pageRepository.existsByUrl(url)) {

                    Page page = new Page();

                    page.setTitle(document.title());

                    page.setDescription(
                            document
                                    .select("meta[name=description]")
                                    .attr("content")
                    );

                    StringBuilder headings = new StringBuilder();

                    document.select("h1, h2, h3").forEach(h ->
                            headings.append(h.text()).append(" ")
                    );

                    page.setHeadings(headings.toString());

                    String content = "";

                    if (document.body() != null) {
                        content = document.body().text();
                    }

                    page.setContent(content);

                    page.setWebsite(host);

                    page.setWordCount(
                            content.isEmpty()
                                    ? 0
                                    : content.split("\\s+").length
                    );

                    page.setUrl(url);

                    page.setIndexedAt(LocalDateTime.now());

                    // SAVE TO POSTGRESQL
                    pageRepository.save(page);

                    pages++;

                    System.out.println(
                            "✅ INDEXED [" + pages + "] "
                                    + document.title()
                    );

                } else {

                    System.out.println(
                            "Already exists: " + url
                    );
                }

                // Find links
                Elements links = document.select("a[href]");

                for (Element link : links) {

                    String next = link.absUrl("href");

                    if (shouldSkip(next)) {
                        continue;
                    }

                    try {

                        String nextHost =
                                new URL(next).getHost();

                        // Same website only
                        if (!nextHost.equals(host)) {
                            continue;
                        }

                    } catch (Exception e) {
                        continue;
                    }

                    if (!visited.contains(next)) {
                        queue.offer(next);
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "❌ FAILED: " + url
                );

                // IMPORTANT:
                // Show the actual error
                e.printStackTrace();
            }
        }

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Finished: " + startUrl);
        System.out.println("Pages indexed: " + pages);
        System.out.println("--------------------------------------");
    }

    private boolean shouldSkip(String url) {

        if (url == null || url.isEmpty()) {
            return true;
        }

        if (url.startsWith("mailto:")) {
            return true;
        }

        if (url.startsWith("javascript:")) {
            return true;
        }

        if (url.contains("#")) {
            return true;
        }

        String lowerUrl = url.toLowerCase();

        if (lowerUrl.endsWith(".pdf")) {
            return true;
        }

        if (lowerUrl.endsWith(".jpg")) {
            return true;
        }

        if (lowerUrl.endsWith(".jpeg")) {
            return true;
        }

        if (lowerUrl.endsWith(".png")) {
            return true;
        }

        if (lowerUrl.endsWith(".gif")) {
            return true;
        }

        if (lowerUrl.endsWith(".zip")) {
            return true;
        }

        if (lowerUrl.contains("/login")) {
            return true;
        }

        if (lowerUrl.contains("/signup")) {
            return true;
        }

        return false;
    }
}