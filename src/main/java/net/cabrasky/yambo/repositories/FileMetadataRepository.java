package net.cabrasky.yambo.repositories;

import net.cabrasky.yambo.models.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findTopByFileNameAndDirectoryOrderByUploadDateDesc(String fileName, String directory);
}
