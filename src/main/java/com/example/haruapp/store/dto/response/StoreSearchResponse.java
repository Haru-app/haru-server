package com.example.haruapp.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreSearchResponse {
    private Long storeId;
    private String storeName;
    private String category;
    private String floor;
    private String image;
}
