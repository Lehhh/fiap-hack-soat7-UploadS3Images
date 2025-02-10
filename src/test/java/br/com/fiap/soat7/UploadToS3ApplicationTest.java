package br.com.fiap.soat7;

import br.com.fiap.soat7.application.services.FileUploadService;
import br.com.fiap.soat7.application.services.RedisService;
import br.com.fiap.soat7.infrastructure.config.DiskUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

@ExtendWith(MockitoExtension.class)
class UploadToS3ApplicationTest {

    @Mock
    private RedisService redisService;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private DiskUtils diskUtils;

    @InjectMocks
    private UploadToS3Application uploadToS3Application;

    @Value("${br.com.fiap.soat7.upload.dir}")
    private String diskFolder = "/mocked/path/%s/%s/%s/";

    @BeforeEach
    public void setUp() {
        diskFolder = "src/test/resources/upload/";
        ReflectionTestUtils.setField(uploadToS3Application, "diskFolder", diskFolder);
    }

    @Test
    void run_shouldProcessQueueAndUploadFiles() throws Exception {
        // Arrange
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String queueItem = userId + ":" + videoId + ":other:" + version;

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("file.zip");

        when(redisService.fetchQueueImageS3()).thenReturn(List.of(queueItem));
        when(diskUtils.listFilesAsMultipartFile(any(Path.class))).thenReturn(List.of(mockFile));
        when(fileUploadService.uploadS3Image(userId, videoId, version, mockFile)).thenReturn("s3url");

        // Act
        uploadToS3Application.run();

        // Assert
        verify(redisService, times(1)).fetchQueueImageS3();
        verify(diskUtils, times(1)).listFilesAsMultipartFile(any(Path.class));
        verify(fileUploadService, times(1)).uploadS3Image(userId, videoId, version, mockFile);
    }

    @Test
    void run_shouldUseOnlyFirstValidZipFile() throws Exception {
        // Arrange
        String userId = "123";
        String videoId = "456";
        String version = "789";
        String queueItem = userId + ":" + videoId + ":other:" + version;

        MultipartFile validFile = mock(MultipartFile.class);
        when(validFile.getOriginalFilename()).thenReturn("valid.zip");
        MultipartFile extraFile = mock(MultipartFile.class);
        when(extraFile.getOriginalFilename()).thenReturn("extra.zip");

        when(redisService.fetchQueueImageS3()).thenReturn(List.of(queueItem));
        when(diskUtils.listFilesAsMultipartFile(any(Path.class))).thenReturn(List.of(validFile, extraFile));
        when(fileUploadService.uploadS3Image(userId, videoId, version, validFile)).thenReturn("s3url");

        // Act
        uploadToS3Application.run();

        // Assert
        verify(redisService, times(1)).fetchQueueImageS3();
        verify(diskUtils, times(1)).listFilesAsMultipartFile(any(Path.class));
        verify(fileUploadService, times(1)).uploadS3Image(userId, videoId, version, validFile);
        verify(fileUploadService, never()).uploadS3Image(userId, videoId, version, extraFile);
    }

    @Test
    void run_shouldHandleEmptyQueue() throws Exception {
        // Arrange
        when(redisService.fetchQueueImageS3()).thenReturn(List.of());

        // Act
        uploadToS3Application.run();

        // Assert
        verify(redisService, times(1)).fetchQueueImageS3();
        verifyNoInteractions(diskUtils);
        verifyNoInteractions(fileUploadService);
    }

    @Test
    void main_shouldInitializeSpringApplication() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Act
            String[] args = new String[]{};
            UploadToS3Application.main(args);

            // Assert
            mockedSpringApplication.verify(() -> SpringApplication.run(UploadToS3Application.class, args), times(1));
        }
    }
}