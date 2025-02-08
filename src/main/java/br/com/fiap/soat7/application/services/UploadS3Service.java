package br.com.fiap.soat7.application.services;

import br.com.fiap.soat7.infrastructure.config.DiskUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UploadS3Service {

	private final RedisService redisService;
	private final DiskUtils diskUtils;
	private final FileUploadService fileUploadService;

	@Value("${br.com.fiap.soat7.upload.dir}")
	private String diskFolder;
	public void uploadVideo(){
		List<String> queueVideoS3 = redisService.fetchQueueVideoS3();
		queueVideoS3.forEach(q -> {
			String[] values = q.split(":");
			String userId = values[0];
			String videoId = values[1];
			String version = values[3];
			List<MultipartFile> multipartFiles = diskUtils.listFilesAsMultipartFile(Path.of(String.format(diskFolder, userId, videoId, version)));
			List<MultipartFile> multipartFilesZip = multipartFiles.stream().filter(m -> m.getOriginalFilename().contains(".zip") && !m.getOriginalFilename().startsWith(".")).toList();
			fileUploadService.uploadS3Video(userId, videoId, version, multipartFilesZip.get(0));
		});
	}


	public void uploadImage(){
		List<String> queueImageS3 = redisService.fetchQueueImageS3();
		queueImageS3.forEach(q -> {
			String[] values = q.split(":");
			String userId = values[0];
			String videoId = values[1];
			String version = values[3];
			List<MultipartFile> multipartFiles = diskUtils.listFilesAsMultipartFile(Path.of(String.format(diskFolder, userId, videoId, version)));
			List<MultipartFile> multipartFilesZip = multipartFiles.stream().filter(m -> m.getOriginalFilename().contains(".zip") && !m.getOriginalFilename().startsWith(".")).toList();
			fileUploadService.uploadS3Image(userId, videoId, version, multipartFilesZip.get(0));
		});
	}



}
