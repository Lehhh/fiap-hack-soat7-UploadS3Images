package br.com.fiap.soat7.domain.enums;

import lombok.Getter;

@Getter
public enum Stage {
	UPLOAD_S3_QUEUE("upload_s3_queue"),
	UPLOAD_S3_IN_PROGRESS("upload_s3_in_progress"),
	UPLOAD_S3_DONE("upload_s3_done"),
	UPLOAD_S3_ERROR("upload_s3_error"),

	UPLOAD_S3_IMAGES_QUEUE("upload_s3_images_queue"),
	UPLOAD_S3_IMAGES_IN_PROGRESS("upload_s3_images_in_progress"),
	UPLOAD_S3_IMAGES_DONE("upload_s3_images_done"),
	UPLOAD_S3_IMAGES_ERROR("upload_s3_images_error");

	private final String name;

	Stage(String name) {
		this.name = name;
	}
}
