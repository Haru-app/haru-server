package com.example.haruapp.store.service;

import com.example.haruapp.store.dto.response.StoreSearchResponse;
import com.example.haruapp.store.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;

    // 대표 카테고리 매핑 Map
    private static final Map<String, List<String>> CATEGORY_MAP = Map.ofEntries(
            entry("음식점", List.of("푸드스트리트", "델리파크", "푸드트럭", "전문식당가")),
            entry("카페", List.of("카페", "베이커리", "디저트", "F&B")),
            entry("푸드마켓", List.of("차", "건강", "와인웍스", "명인명촌", "쌀", "떡", "테이스티 서울 마켓")),
            entry("캐릭터", List.of("영 캐릭터", "컨템")),
            entry("패션", List.of("란제리", "글로벌 패션", "남성 럭셔리패션", "남/여 복합", "컨템포러리", "여성패션", "남성패션", "진", "스트리트", "SPA", "언더웨어", "잡화", "편집샵")),
            entry("패션잡화", List.of("워치", "패션잡화", "ACC", "주얼리", "슈즈", "백", "갤러리", "럭셔리부띠끄")),
            entry("화장품", List.of("화장품")),
            entry("공간/서비스", List.of("컬쳐", "공간", "VIP 서비스")),
            entry("아동", List.of("아동", "유아")),
            entry("라이프스타일", List.of("컬쳐", "라이프스타일")),
            entry("리빙/가전", List.of("리빙", "가전", "가구", "침구", "식기", "홈데코")),
            entry("스포츠", List.of("스포츠", "아웃도어", "골프")),
            entry("편의시설", List.of("기타", "편의시설")),
            entry("기타", List.of("기타"))
    );

    // 대표 카테고리 반환
    public List<String> getRepresentativeCategories() {
        return new ArrayList<>(CATEGORY_MAP.keySet());
    }

    /**
     * 매장 검색
     * - 카테고리별 매장 리스트 검색
     * - 매장 이름으로 검색
     * - 카테고리 + 매장 검색
     * */
    public List<StoreSearchResponse> searchStores(String category, String keyword) {
        List<String> rawCategories = null;
        if (category != null && CATEGORY_MAP.containsKey(category)) {
            rawCategories = CATEGORY_MAP.get(category);
        }
        return storeMapper.findStores(rawCategories, keyword);
    }
}
