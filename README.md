# 🔎 SearchIT

A lightweight technical search engine built with **Java, Spring Boot, PostgreSQL, Jsoup, and React**.

SearchIT crawls trusted technical websites, indexes their content, and provides fast keyword-based search results through a clean web interface.

## 🚀 Features

- 🌐 Website crawling with Jsoup
- 🔎 Full-text search using PostgreSQL `tsvector`
- ⚡ REST APIs with Spring Boot
- 💡 Search suggestions
- 📄 Search results with title, URL & snippets
- 🎨 Modern React + Tailwind UI
- ✨ Animated particle background
- 🗄️ PostgreSQL database

## 🛠️ Tech Stack

**Backend:** Java 21, Spring Boot, Jsoup, JPA  
**Frontend:** React, Vite, Tailwind CSS  
**Database:** PostgreSQL  
**Search:** PostgreSQL Full-Text Search

## ▶️ Run Locally

### Backend
```bash
mvn spring-boot:run
Frontend
npm install
npm run dev

Backend runs on localhost:8080 and frontend on the Vite development server.

📌 Future Improvements
Redis caching
Search history
Ranking improvements
Pagination
More indexed websites
Deployment

### Backend
```bash
mvn spring-boot:run
