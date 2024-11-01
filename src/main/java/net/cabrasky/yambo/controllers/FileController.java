package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.FileMetadata;
import net.cabrasky.yambo.services.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download/{dir}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String dir,
            @PathVariable String filename) {

        Optional<FileMetadata> fileMetadataOpt = fileService.getLatestFileMetadata(filename, dir);
        if (fileMetadataOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FileMetadata fileMetadata = fileMetadataOpt.get();
        Path filePath = Paths.get(fileMetadata.getRealPath());

        if (!Files.exists(filePath)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
