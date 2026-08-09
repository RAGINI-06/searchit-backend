//package com.searchengine.controller;
//
//import com.searchengine.dto.SearchResponse;
//import com.searchengine.service.SearchService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/search")
//public class SearchController {
//
//    private final SearchService service;
//
//    public SearchController(SearchService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    public List<SearchResponse> search(
//            @RequestParam String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return service.search(keyword, page, size);
//    }
//    @GetMapping("/suggestions")
//    public List<String> suggestions(
//            @RequestParam String query
//    ) {
//
//        return service.getSuggestions(query);
//
//    }
//}
//package com.searchengine.controller;

//import com.searchengine.dto.SearchResponse;
//import com.searchengine.service.SearchService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/search")
//@CrossOrigin(origins = "http://localhost:5173")
//public class SearchController {
//
//    private final SearchService searchService;
//
//    public SearchController(SearchService searchService) {
//        this.searchService = searchService;
//    }
//
//    @GetMapping
//    public List<SearchResponse> search(
//            @RequestParam String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return searchService.search(keyword, page, size);
//    }
//}
package com.searchengine.controller;

import com.searchengine.dto.SearchResponse;
import com.searchengine.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping
    public List<SearchResponse> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.search(keyword, page, size);
    }

    @GetMapping("/suggestions")
    public List<String> suggestions(
            @RequestParam String query) {

        return service.getSuggestions(query);
    }
}