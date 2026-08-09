package com.searchengine.config;

import java.util.List;

public class WebsiteList {

    public static final List<String> WEBSITES = List.of(

            // =========================
            // Java               
            // =========================
            "https://docs.oracle.com/en/java/",
            "https://spring.io/guides",
            "https://docs.spring.io/spring-boot/index.html",
            "https://www.baeldung.com",
            "https://www.javaguides.net",
            "https://howtodoinjava.com",
            "https://mkyong.com",

            // =========================
            // Web Development
            // =========================
            "https://developer.mozilla.org",
            "https://react.dev",
            "https://nextjs.org/docs",
            "https://nodejs.org/docs/latest/api/",
            "https://tailwindcss.com/docs",

            // =========================
            // DSA
            // =========================
            "https://cp-algorithms.com",
            "https://www.geeksforgeeks.org",
            "https://usaco.guide",
            "https://cses.fi/book/index.php",

            // =========================
            // Programming
            // =========================
            "https://www.tutorialspoint.com",
            "https://www.w3schools.com",
            "https://www.freecodecamp.org/news",
            "https://www.digitalocean.com/community/tutorials",

            // =========================
            // Databases
            // =========================
            "https://www.postgresql.org/docs/",
            "https://dev.mysql.com/doc/",
            "https://www.mongodb.com/docs/",
            "https://redis.io/docs/",

            // =========================
            // DevOps
            // =========================
            "https://docs.docker.com/",
            "https://kubernetes.io/docs/",
            "https://git-scm.com/doc",
            "https://maven.apache.org/guides/",
            "https://gradle.org/guides/",

            // =========================
            // AI
            // =========================
            "https://huggingface.co/docs",
            "https://pytorch.org/docs",
            "https://www.tensorflow.org/guide",

            // =========================
            // Languages
            // =========================
            "https://go.dev/doc/",
            "https://doc.rust-lang.org/book/",
            "https://kotlinlang.org/docs/home.html"

    );

    private WebsiteList() {
        // Prevent instantiation
    }
}