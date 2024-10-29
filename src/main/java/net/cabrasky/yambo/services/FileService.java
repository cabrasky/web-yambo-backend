package net.cabrasky.yambo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.cabrasky.yambo.models.FileMetadata;
import net.cabrasky.yambo.repositories.FileMetadataRepository;

@Service
public class FileService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    public Optional<FileMetadata> getLatestFileMetadata(String fileName, String directory) {
        return fileMetadataRepository.findTopByFileNameAndDirectoryOrderByUploadDateDesc(fileName, directory);
    }
}
