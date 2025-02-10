package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Config, "bucket", "test-bucket");
        ReflectionTestUtils.setField(s3Config, "region", "us-east-1");
        ReflectionTestUtils.setField(s3Config, "accessKey", "test-access-key");
        ReflectionTestUtils.setField(s3Config, "secretKey", "test-secret-key");
        ReflectionTestUtils.setField(s3Config, "sessionToken", "test-session-token");
    }

    @Test
    void testS3ClientInitialization() {
        S3Client s3Client = s3Config.s3Client();
        assertNotNull(s3Client, "S3Client should not be null");
    }

    @Test
    void testSessionCredentials() {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(
                "test-access-key",
                "test-secret-key",
                "test-session-token"
        );
        assertNotNull(credentials, "AwsSessionCredentials should not be null");
    }
}
