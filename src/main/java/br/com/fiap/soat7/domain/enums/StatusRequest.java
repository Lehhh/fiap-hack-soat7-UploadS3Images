package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

@Getter
public enum StatusRequest {
	UPLOAD_S3_STATUS("video/s3/status"),
	UPLOAD_S3_QUEUE("video/s3/queue"),
	UPLOAD_S3_IMAGES_STATUS("images/status"),
	UPLOAD_S3_IMAGES_QUEUE("images/queue");


	private final String endPoint;
	StatusRequest(String endPoint) {
		this.endPoint = endPoint;
	}
}
