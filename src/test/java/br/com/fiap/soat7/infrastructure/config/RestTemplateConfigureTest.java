package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the RestTemplateConfigure class.
 * <p>
 * This test class verifies the RestTemplate bean creation by mocking the configuration
 * and using the actual method under test.
 */
public class RestTemplateConfigureTest {

    @Test
    void testRestTemplateBeanCreationUsingMock() {
        // Mock the configuration class
        RestTemplateConfigure mockConfig = Mockito.mock(RestTemplateConfigure.class);

        // Use the actual method to create the RestTemplate
        Mockito.when(mockConfig.restTemplate()).thenCallRealMethod();

        // Retrieve the RestTemplate instance
        RestTemplate restTemplate = mockConfig.restTemplate();

        // Validate that the RestTemplate object is created
        assertNotNull(restTemplate, "The RestTemplate bean should not be null");
    }

    @Test
    void testRestTemplateBeanCreation() {
        // Create an instance of the configuration class
        RestTemplateConfigure config = new RestTemplateConfigure();

        // Use the restTemplate method to create the RestTemplate
        RestTemplate restTemplate = config.restTemplate();

        // Verify the RestTemplate bean is created
        assertNotNull(restTemplate, "The RestTemplate bean should not be null");
    }

}