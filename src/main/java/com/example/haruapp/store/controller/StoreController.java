package com.example.haruapp.store.controller;

import com.example.haruapp.store.dto.response.StoreSearchResponse;
import com.example.haruapp.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 대표 카테고리 반환
     * */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategoryList() {
        List<String> categories = storeService.getRepresentativeCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 매장 검색
     * - 카테고리별 매장 리스트 검색
     * - 매장 이름으로 검색
     * - 카테고리 + 매장 검색
     * - 페이지 기반 페이지네이션
     * - 페이지 당 매장 수 5로 고정
     * */
    @GetMapping
    public ResponseEntity<List<StoreSearchResponse>> searchStores(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page
    ) {
        int size = 5;
        List<StoreSearchResponse> result = storeService.searchStores(category, keyword, page, size);
        return ResponseEntity.ok(result);
    }
}