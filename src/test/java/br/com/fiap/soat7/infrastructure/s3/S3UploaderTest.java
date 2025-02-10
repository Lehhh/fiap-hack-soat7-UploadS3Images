package br.com.fiap.soat7.infrastructure.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3UploaderTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private S3Uploader s3Uploader;

    @Value("test-bucket")
    private String bucket = "test-bucket";

    @Value("http://localhost:4566")
    private String endpoint = "http://localhost:4566/";

    @Value("uploads/%s/%s/%s/")
    private String s3Folder = "uploads/%s/%s/%s/";

    @Value("/tmp/uploads/%s/%s/%s/")
    private String diskFolder = "/tmp/uploads/%s/%s/%s/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        s3Uploader = new S3Uploader(s3Client);
        s3Uploader.bucket = bucket;
        s3Uploader.endpoint = endpoint;
        s3Uploader.s3Folder = s3Folder;
        s3Uploader.diskFolder = diskFolder;
    }

    @Test
    void testUploadSuccess() throws Exception {
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String fileName = "test.mp4";


        when(multipartFile.getOriginalFilename()).thenReturn(fileName);

        String expectedKeyName = String.format(s3Folder, userId, videoId, version) + fileName;
        String expectedUrl = endpoint.endsWith("/")
                ? endpoint + bucket + "/" + expectedKeyName
                : endpoint + "/" + bucket + "/" + expectedKeyName;

        // Act: Call the method to test
        String result = s3Uploader.upload(userId, videoId, version, multipartFile);

        // Assert: Verify all interactions and validate the results
        verify(multipartFile, times(2)).getOriginalFilename();
        assertEquals(expectedUrl, result);

    }

    @Test
    void testUploadFailure() throws Exception {
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String fileName = "test.mp4";

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);

        // Simulate failure in s3Client.putObject
        doThrow(new RuntimeException("Upload failed")).when(s3Client).putObject(any(PutObjectRequest.class), any(Path.class));

        String result = s3Uploader.upload(userId, videoId, version, multipartFile);

        // Ensure s3Client.putObject is called exactly once
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(Path.class));

        // Assert the result still returns the proper URL format, despite the failed upload
        String expectedKeyName = String.format(s3Folder, userId, videoId, version) + fileName;
        String expectedUrl = endpoint + bucket + "/" + expectedKeyName;
        assertEquals(expectedUrl, result);
    }
}
