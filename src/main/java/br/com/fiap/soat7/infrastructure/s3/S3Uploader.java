package br.com.fiap.soat7.infrastructure.s3;

import br.com.fiap.soat7.infrastructure.config.FileToMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Value("${aws.s3.endpoint}")
	private String endpoint;

	@Value(("${br.com.fiap.soat7.upload.s3-folder}"))
	private String s3Folder;

	@Value("${br.com.fiap.soat7.upload.dir}")
	private String diskFolder;


	private final S3Client s3Client;
	public String upload(String userId, String videoId, String version, MultipartFile file) {
		String keyName = String.format(s3Folder, userId,videoId,version) + file.getOriginalFilename();
		System.out.println(keyName);
		try {
			s3Client.putObject(
					PutObjectRequest.builder()
							.bucket(bucket)
							.key(keyName)
							.build(),
					Paths.get(String.format(diskFolder, userId, videoId, version) + file.getOriginalFilename())
			);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return endpoint + bucket + "/" + keyName;
	}
}
