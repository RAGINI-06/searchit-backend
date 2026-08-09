package com.searchengine.service;

import com.searchengine.entity.Page;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

    public double calculateScore(Page page, String keyword) {

        double score = 0;

        keyword = keyword.toLowerCase();

        if(page.getTitle().toLowerCase().contains(keyword))
            score += 10;

        if(page.getHeadings().toLowerCase().contains(keyword))
            score += 7;

        if(page.getDescription().toLowerCase().contains(keyword))
            score += 5;

        if(page.getContent().toLowerCase().contains(keyword))
            score += 2;

        score += Math.min(page.getWordCount()/1000.0,2);

        return score;

    }

}