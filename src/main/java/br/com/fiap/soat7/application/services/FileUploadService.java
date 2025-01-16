package br.com.fiap.soat7.application.services;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.Stage;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Log4j2
public class FileUploadService {

	private final S3Uploader s3Uploader;
	private final RedisService redisService;

	public String uploadS3Video( String userId, String videoId, String version,  MultipartFile file) {
		String uploadResponse = null;
		redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_IN_PROGRESS), StatusRequest.UPLOAD_S3_STATUS);
		try{
			uploadResponse = s3Uploader.upload(userId, videoId, version, file);
			redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_DONE), StatusRequest.UPLOAD_S3_STATUS);
			log.info("Upload realizado com sucesso: {}/{}/{}", userId, videoId, version );
			return uploadResponse;
		}
		catch (Exception e){
			redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_ERROR), StatusRequest.UPLOAD_S3_STATUS);
			return null;
		}
	}

	public String uploadS3Image( String userId, String videoId, String version,  MultipartFile file) {
		String uploadResponse = null;
		redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_IMAGES_IN_PROGRESS), StatusRequest.UPLOAD_S3_IMAGES_STATUS);
		try{
			uploadResponse = s3Uploader.upload(userId, videoId, version, file);
			redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_IMAGES_DONE), StatusRequest.UPLOAD_S3_IMAGES_STATUS);
			return uploadResponse;
		}
		catch (Exception e){
			redisService.sendStatus(new InfoVideo(videoId, userId, version, Stage.UPLOAD_S3_IMAGES_ERROR), StatusRequest.UPLOAD_S3_IMAGES_STATUS);
			return null;
		}
	}
}
