package com.example.haruapp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Component
public class GcpStorageUtil {

	private final String bucketName = "haru_image";

	private final Storage storage;

	public GcpStorageUtil() {

		try {
			this.storage = StorageOptions.newBuilder()
				.setCredentials(ServiceAccountCredentials.fromStream(
					new FileInputStream("/app/resource/firebase/haru-firebase-key.json")
				))
				.build()
				.getService();
		} catch (IOException e) {
			throw new RuntimeException("GCP 인증 실패", e);
		}
	}

	public String uploadBase64Image(String base64) {

		byte[] imageBytes = Base64.getDecoder().decode(base64);
		String filename = UUID.randomUUID() + ".png";
		String blobName = "emotion/" + filename;

		BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName)
			.setContentType("image/png")
			.build();

		storage.create(blobInfo, imageBytes);

		return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);
	}
}