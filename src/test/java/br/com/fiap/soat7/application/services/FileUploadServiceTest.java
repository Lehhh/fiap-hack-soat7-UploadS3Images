package br.com.fiap.soat7.application.services;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileUploadServiceTest {

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private FileUploadService fileUploadService;

    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        file = mock(MultipartFile.class);
    }

    @Test
    void uploadS3Video_success() {
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String expectedResponse = "s3://bucket/123/456/789";

        when(s3Uploader.upload(userId, videoId, version, file)).thenReturn(expectedResponse);

        String response = fileUploadService.uploadS3Video(userId, videoId, version, file);

        assertEquals(expectedResponse, response);
        verify(redisService, times(2)).sendStatus(any(InfoVideo.class), eq(StatusRequest.UPLOAD_S3_STATUS));
        verify(s3Uploader, times(1)).upload(userId, videoId, version, file);
    }

    @Test
    void uploadS3Video_failure() {
        String userId = "123";
        String videoId = "456";
        String version = "789";

        when(s3Uploader.upload(userId, videoId, version, file)).thenThrow(new RuntimeException("Upload failed"));

        String response = fileUploadService.uploadS3Video(userId, videoId, version, file);

        assertNull(response);
        verify(redisService, times(2)).sendStatus(any(), eq(StatusRequest.UPLOAD_S3_STATUS));
    }

    @Test
    void uploadS3Image_success() {
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String expectedResponse = "s3://bucket/123/456/789/images";

        when(s3Uploader.upload(userId, videoId, version, file)).thenReturn(expectedResponse);

        String response = fileUploadService.uploadS3Image(userId, videoId, version, file);

        assertEquals(expectedResponse, response);
        verify(redisService, times(2)).sendStatus(any(), eq(StatusRequest.UPLOAD_S3_IMAGES_STATUS));
        verify(s3Uploader, times(1)).upload(userId, videoId, version, file);
    }

    @Test
    void uploadS3Image_failure() {
        String userId = "123";
        String videoId = "456";
        String version = "789";

        when(s3Uploader.upload(userId, videoId, version, file)).thenThrow(new RuntimeException("Upload failed"));

        String response = fileUploadService.uploadS3Image(userId, videoId, version, file);

        assertNull(response);
        verify(redisService, times(2)).sendStatus(any(), eq(StatusRequest.UPLOAD_S3_IMAGES_STATUS));
    }
}
