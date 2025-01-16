package br.com.fiap.soat7;

import br.com.fiap.soat7.application.services.FileUploadService;
import br.com.fiap.soat7.application.services.RedisService;
import br.com.fiap.soat7.infrastructure.config.DiskUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class UploadToS3Application implements CommandLineRunner {

	private final RedisService redisService;
	private final FileUploadService fileUploadService;
	private final DiskUtils diskUtils;

	@Value("${br.com.fiap.soat7.upload.dir}")
	private String diskFolder;


	public static void main(String[] args) {
		SpringApplication.run(UploadToS3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
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
