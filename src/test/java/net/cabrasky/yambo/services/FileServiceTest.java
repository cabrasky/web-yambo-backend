package net.cabrasky.yambo.services;

import net.cabrasky.yambo.models.FileMetadata;
import net.cabrasky.yambo.repositories.FileMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @InjectMocks
    private FileService fileMetadataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestFileMetadata() {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(1L);
        fileMetadata.setFileName("example.txt");
        fileMetadata.setRealPath("/path/to/example.txt");
        fileMetadata.setDirectory("files");
        fileMetadata.setUploadDate(LocalDateTime.now());

        when(fileMetadataRepository.findTopByFileNameAndDirectoryOrderByUploadDateDesc("example.txt", "files"))
                .thenReturn(Optional.of(fileMetadata));

        // Act
        Optional<FileMetadata> result = fileMetadataService.getLatestFileMetadata("example.txt", "files");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("example.txt", result.get().getFileName());
        assertEquals("/path/to/example.txt", result.get().getRealPath());
        assertEquals("files", result.get().getDirectory());
    }

    @Test
    void testGetLatestFileMetadataNotFound() {
        // Arrange
        when(fileMetadataRepository.findTopByFileNameAndDirectoryOrderByUploadDateDesc("nonexistent.txt", "files"))
                .thenReturn(Optional.empty());

        // Act
        Optional<FileMetadata> result = fileMetadataService.getLatestFileMetadata("nonexistent.txt", "files");

        // Assert
        assertTrue(result.isEmpty());
    }
}
