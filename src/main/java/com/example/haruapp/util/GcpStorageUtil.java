package com.example.haruapp.util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GcpStorageUtil {

	private final Storage storage = StorageOptions.getDefaultInstance().getService();

	private final String bucketName = "haru_image";

	public String upload(MultipartFile file) {

		String folder = "emotion/" + LocalDate.now();
		String originalName = file.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf("."));
		String filename = UUID.randomUUID() + extension;
		String blobName = folder + "/" + filename;

		try {
			BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName)
				.setContentType(file.getContentType())
				.build();
			storage.create(blobInfo, file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("파일 업로드 실패", e);
		}

		return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
	}
}
