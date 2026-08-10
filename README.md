# 🔎 SearchIT

**A domain-focused search engine built for developers.**

SearchIT crawls, indexes, and ranks technical content — Java, Spring Boot, React, JavaScript, databases, DevOps, and more — so you can search programming knowledge the way you'd search Google, but scoped entirely to high-quality developer resources.

**🔗 Live Demo:** [search-it-a-search-engine-for-techi.vercel.app](https://search-it-a-search-engine-for-techi.vercel.app)

---


## Why SearchIT

General-purpose search engines index the entire internet. SearchIT does the opposite — it's a **vertical search engine** focused exclusively on developer and technical content (documentation, tutorials, interview prep, framework guides), which means:

- Higher signal-to-noise ratio in results
- Full control over crawled sources and content quality
- A realistic, end-to-end demonstration of how search engines actually work — crawling, indexing, ranking, and querying — without needing Google-scale infrastructure

SearchIT is a **retrieval system, not a generative one**. It doesn't synthesize answers with an LLM — it finds, ranks, and returns the most relevant indexed pages with snippets and source links, much like a focused Bing/Google.

---

## Architecture

### Data Collection Pipeline

```
Technical Websites → Crawler (Jsoup) → Content Extraction → PostgreSQL (pages) → search_vector
```

### Search Request Pipeline

```
User → React (Vite) → REST API → Spring Boot → PostgreSQL Full-Text Search → Ranking → JSON → React UI
```

### Full System Diagram

```
                    ┌──────────────────────┐
                    │        User           │
                    │ "dependency injection │
                    │       in java"        │
                    └──────────┬────────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   React + Vite        │
                    │   (Vercel)             │
                    └──────────┬────────────┘
                               │ REST (Axios)
                               ▼
                    ┌──────────────────────┐
                    │   Spring Boot API     │
                    │   (Render, Docker)    │
                    └──────────┬────────────┘
                 ┌─────────────┴─────────────┐
                 ▼                           ▼
        ┌─────────────────┐        ┌─────────────────┐
        │  Search Service  │        │ Search History  │
        └────────┬────────┘        └────────┬────────┘
                 │                          │
                 ▼                          ▼
        ┌──────────────────────────────────────────┐
        │           PostgreSQL (Neon)               │
        │  pages | search_history | search_vector   │
        └──────────────────────────────────────────┘
```

### Deployment Topology

```
Vercel (React)  ──HTTPS──▶  Render (Spring Boot, Docker)  ──JDBC──▶  Neon (PostgreSQL)
```

---

## Tech Stack

| Layer | Technologies |
|---|---|
| **Frontend** | React, Vite, Tailwind CSS, React Router, Axios, Lucide React |
| **Backend** | Java 21, Spring Boot, Spring Data JPA, Spring Web, Maven |
| **Search** | PostgreSQL Full-Text Search, `tsvector`, `websearch_to_tsquery`, `ts_rank`, LIKE fallback |
| **Database** | PostgreSQL (hosted on Neon) |
| **Crawling** | Java, Jsoup (HTML parsing, recursive crawling) |
| **DevOps** | Docker, Render, Vercel, GitHub, environment-based config |

---

## How Search Works

### 1. Crawling
A Jsoup-based crawler visits seed URLs (e.g. Spring docs, MDN, HowToDoInJava), downloads HTML, and extracts:

- Title, headings, description, body content
- Word count and source website
- Outbound links, which feed the next crawl cycle

Extracted pages are persisted to the `pages` table in PostgreSQL.

### 2. Indexing
Every insert/update on `pages` triggers generation of a `search_vector` — a PostgreSQL `tsvector` built from the title and content — enabling fast, linguistically-aware full-text search instead of naive substring matching.

```sql
to_tsvector(
    'english',
    COALESCE(NEW.title, '') || ' ' || COALESCE(NEW.content, '')
)
```

### 3. Querying
Search combines PostgreSQL full-text search with a `LIKE`-based fallback across multiple fields, so natural-language queries (`websearch_to_tsquery`) and partial substring matches both surface results:

```sql
SELECT * FROM pages
WHERE
    search_vector @@ websearch_to_tsquery('english', :keyword)
    OR LOWER(title)       LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(headings)    LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(content)     LIKE LOWER(CONCAT('%', :keyword, '%'))
```

### 4. Ranking
Results are scored by field-weighted priority plus PostgreSQL's relevance score:

- **Title match** → highest priority
- **Heading match** → medium priority
- **Content-only match** → lowest priority
- Ties broken by `ts_rank(search_vector, websearch_to_tsquery(...))`

### 5. Snippets & Suggestions
- Results return short, contextual **snippets** around the matched keyword rather than full page content.
- A `/suggestions` endpoint offers autocomplete as the user types, matching on title prefixes.
- Every search is logged to `search_history`, laying the groundwork for trending/popular search analytics.

---

## API Reference

### Search

```
GET /api/search?keyword=dependency+injection+java&page=0&size=10
```

**Response**

```json
[
  {
    "title": "Spring Beans and Dependency Injection",
    "url": "https://docs.spring.io/...",
    "snippet": "Spring provides a powerful mechanism for dependency injection..."
  }
]
```

Only a lean DTO (`SearchResponse`) is exposed — internal fields like `id`, `search_vector`, and `indexed_at` are never leaked to the client.

### Suggestions

```
GET /api/search/suggestions?query=jav
```

**Response**

```json
["Java", "Java 14", "Java Interview Questions", "Java Spring Boot"]
```

### Pagination
`page` and `size` query params control result batches, which matters once the index grows past tens of thousands of pages.

---

## Database Schema

**`pages`**

| Column | Description |
|---|---|
| `id` | Primary key |
| `title` | Page title |
| `url` | Source URL |
| `content` | Extracted body text |
| `description` | Page description/meta |
| `headings` | Extracted heading text |
| `website` | Source site name |
| `word_count` | Content length |
| `indexed_at` | Timestamp of indexing |
| `search_vector` | Generated `tsvector` for full-text search |

**`search_history`**

| Column | Description |
|---|---|
| `id` | Primary key |
| `keyword` | Search term entered |
| `searched_at` | Timestamp |

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- Node.js + npm
- A PostgreSQL instance (e.g. [Neon](https://neon.tech))

### Backend

```bash
git clone <your-repo-url>
cd searchit-backend

# Set DB credentials via environment variables — never commit them
export SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<db>
export SPRING_DATASOURCE_USERNAME=<user>
export SPRING_DATASOURCE_PASSWORD=<password>

mvn spring-boot:run
```

### Frontend

```bash
cd searchit-frontend
npm install
npm run dev
```

### Crawler
Run the crawler module with a seed list of technical sites to populate the `pages` table before searching locally.

> ⚠️ **Security note:** Database credentials should always come from environment variables, never hardcoded in `application.properties` or committed to Git. If a DB password has ever been exposed (e.g. shared in a chat, commit, or log), rotate it immediately in your Neon dashboard.

---

## Deployment

| Component | Platform |
|---|---|
| Frontend | Vercel |
| Backend | Render (Docker container, Java 21 base image) |
| Database | Neon (PostgreSQL) |

CORS is scoped to the production Vercel origin (not `*`) so only the deployed frontend can call the API in production.

---

## Roadmap

**SearchIT + RAG (Retrieval-Augmented Generation)**

A natural next step is layering an LLM on top of the existing retrieval pipeline:

```
Query → SearchIT Retrieval → Top-K relevant pages → Context extraction → LLM → Synthesized answer + sources
```

This would let SearchIT go from "here are 10 relevant pages" to "here's a direct explanation, with code examples, cited from those pages" — turning it into a technical RAG assistant rather than a pure search index.

Other planned improvements:
- Trending/popular search analytics from `search_history`
- Expanded crawl sources (Python, SQL, Docker, Kubernetes, Git, System Design, DSA)
- Query auto-correction and typo tolerance
- Result caching for high-frequency queries

---


## License

Specify your license here (e.g. MIT).
