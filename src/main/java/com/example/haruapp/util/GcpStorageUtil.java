package com.example.haruapp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class GcpStorageUtil {

	private final String bucketName = "haru_image";

	private Storage storage = StorageOptions.getDefaultInstance().getService();

	public GcpStorageUtil() {

		try {
			this.storage = StorageOptions.newBuilder()
				.setCredentials(ServiceAccountCredentials.fromStream(
					new FileInputStream("src/main/resources/gcp-key.json")
				))
				.build()
				.getService();
		} catch (IOException e) {
			throw new RuntimeException("GCP 인증 실패", e);
		}
	}

	public String upload(MultipartFile file) {

		String folder = "emotion";
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
