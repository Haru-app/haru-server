package com.example.haruapp.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
	private Long storeId;
	private String storeName;
	private String category;
	private String floor;
	private String description;
	private String hashTags;
	private String image;
}
