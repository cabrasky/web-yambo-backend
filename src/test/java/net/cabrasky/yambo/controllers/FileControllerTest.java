package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.FileMetadata;
import net.cabrasky.yambo.services.FileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        
        // Create a temporary file for testing
        tempFile = Files.createTempFile("test-file", ".txt");
        Files.writeString(tempFile, "This is a test file."); 
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary file after the test
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testDownloadFile() throws Exception {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setId(1L);
        fileMetadata.setFileName("test-file.txt");
        fileMetadata.setRealPath(tempFile.toString());  // Set the real path to the temp file
        fileMetadata.setDirectory("files");

        when(fileService.getLatestFileMetadata("test-file.txt", "files"))
                .thenReturn(Optional.of(fileMetadata));

        // Act & Assert
        mockMvc.perform(get("/files/download/files/test-file.txt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test-file.txt\""))
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().string("This is a test file."));  // Ensure content matches
    }

    @Test
    void testDownloadFileNotFound() throws Exception {
        // Arrange
        when(fileService.getLatestFileMetadata("nonexistent.txt", "files"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/files/download/files/nonexistent.txt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
