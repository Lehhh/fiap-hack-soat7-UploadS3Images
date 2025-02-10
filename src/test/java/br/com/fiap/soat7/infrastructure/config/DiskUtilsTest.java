package br.com.fiap.soat7.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class DiskUtilsTest {

    @Test
    void testListFilesAsMultipartFile_WithValidFiles_ReturnsMultipartFileList() throws IOException {
        // Arrange
        DiskUtils diskUtils = new DiskUtils();
        Path testFolderPath = Files.createTempDirectory("testFolder");
        File tempFile1 = Files.createTempFile(testFolderPath, "file1", ".txt").toFile();
        File tempFile2 = Files.createTempFile(testFolderPath, "file2", ".txt").toFile();

        // Act
        List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(testFolderPath);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof MultipartFile);
        assertTrue(result.get(1) instanceof MultipartFile);

        // Cleanup
        tempFile1.delete();
        tempFile2.delete();
        Files.delete(testFolderPath);
    }

    @Test
    void testListFilesAsMultipartFile_WithEmptyDirectory_ReturnsEmptyList() throws IOException {
        // Arrange
        DiskUtils diskUtils = new DiskUtils();
        Path emptyFolderPath = Files.createTempDirectory("emptyFolder");

        // Act
        List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(emptyFolderPath);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Cleanup
        Files.delete(emptyFolderPath);
    }

    @Test
    void testListFilesAsMultipartFile_WithIOException_ReturnsEmptyList() {
        // Arrange
        DiskUtils diskUtils = new DiskUtils(); // Class under test

        // Mock the static Files.list() method to throw an IOException
        Path invalidPath = mock(Path.class);
        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.list(invalidPath)).thenThrow(new IOException("Cannot access path"));

            // Act
            List<MultipartFile> result = diskUtils.listFilesAsMultipartFile(invalidPath);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}