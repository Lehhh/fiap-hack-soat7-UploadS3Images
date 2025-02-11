package br.com.fiap.soat7.application.services;

import br.com.fiap.soat7.application.services.RedisService;
import br.com.fiap.soat7.application.services.UploadS3Service;
import br.com.fiap.soat7.application.usecases.UploadCase;
import br.com.fiap.soat7.infrastructure.config.UploadToS3Properties;
import br.com.fiap.soat7.domain.enums.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadCaseTest {

    @Mock
    private UploadToS3Properties props;

    @Mock
    private UploadS3Service uploadS3Service;

    @InjectMocks
    private UploadCase uploadCase;

    @Mock
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        // Any setup can be done here if needed
    }

//    @Test
//    void run_shouldUploadVideoWhenStepIsVideo() throws Exception {
//        // Arrange
//        when(props.getStep()).thenReturn(Step.VIDEO.name());
//        uploadCase = new UploadCase(props, uploadS3Service);
//        // Act
//
//        // Assert
//        verify(uploadS3Service, times(1)).uploadVideo();
//        verify(uploadS3Service, never()).uploadImage();
//    }

//    @Test
//    void run_shouldLogErrorWhenStepIsInvalid() throws Exception {
//        // Arrange
//        when(props.getStep()).thenReturn("INVALID");
//
//        // Act
//
//        // Assert
//        verify(uploadS3Service, never()).uploadVideo();
//        verify(uploadS3Service, never()).uploadImage();
//        // You can also verify that the log.error was called if you have a way to capture logs
//    }
}