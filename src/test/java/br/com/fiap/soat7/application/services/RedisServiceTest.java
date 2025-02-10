package br.com.fiap.soat7.application.services;

import br.com.fiap.soat7.domain.dto.InfoVideo;
import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.UploadToS3Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class RedisServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UploadToS3Properties props;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(props.getRedisMidUrl()).thenReturn("http://localhost:8080/");
    }

    @Test
    void sendStatus_shouldReturnTrue_whenRequestIsSuccessful() {
        InfoVideo infoVideo = new InfoVideo("123", "456", "789",null);
        StatusRequest statusRequest = StatusRequest.UPLOAD_S3_STATUS;

        when(restTemplate.exchange(
                eq("http://localhost:8080/" + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("Success"));

        Boolean result = redisService.sendStatus(infoVideo, statusRequest);

        assertTrue(result);
    }

    @Test
    void sendStatus_shouldReturnFalse_whenRequestFails() {
        InfoVideo infoVideo = new InfoVideo("123", "456", "789", null);
        StatusRequest statusRequest = StatusRequest.UPLOAD_S3_STATUS;

        when(restTemplate.exchange(
                eq("http://localhost:8080/" + statusRequest.getEndPoint()),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Error"));

        Boolean result = redisService.sendStatus(infoVideo, statusRequest);

        assertFalse(result);
    }

    @Test
    void fetchQueueVideoS3_shouldReturnListOfVideos() {
        List<String> expectedVideos = Arrays.asList("video1", "video2");

        when(restTemplate.exchange(
                eq("http://localhost:8080/" + StatusRequest.UPLOAD_S3_QUEUE.getEndPoint()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(expectedVideos));

        List<String> result = redisService.fetchQueueVideoS3();

        assertEquals(expectedVideos, result);
    }

    @Test
    void fetchQueueImageS3_shouldReturnListOfImages() {
        List<String> expectedImages = Arrays.asList("image1", "image2");

        when(restTemplate.exchange(
                eq("http://localhost:8080/" + StatusRequest.UPLOAD_S3_IMAGES_QUEUE.getEndPoint()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(expectedImages));

        List<String> result = redisService.fetchQueueImageS3();

        assertEquals(expectedImages, result);
    }
}
