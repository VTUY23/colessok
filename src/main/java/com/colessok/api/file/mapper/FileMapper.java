package com.colessok.api.file.mapper;

import java.io.IOException;
import java.nio.file.Paths;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.file.dto.FileInfo;
import com.colessok.api.file.dto.response.FileResponse;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.repository.FileRepository;

@Mapper(componentModel = "spring")
public abstract class FileMapper {
    public abstract File toFile(FileInfo fileInfo);

    @Autowired
    private FileRepository fileRepository;

    public File mapAndStoreFile(MultipartFile file) throws IOException {
        FileInfo fileInfo = fileRepository.store(file);

        return toFile(fileInfo);
    }

    @Mapping(target = "originalFileName", source = "path", qualifiedByName = "getFileName")
    @Mapping(target = "url", source = "id", qualifiedByName = "generateUrl")
    @Mapping(target = "type", source = "contentType")
    public abstract FileResponse toFileResponse(File file);

    @Named("getFileName")
    public String getFileName(String path) {
        if (path != null && !path.isEmpty()) {
            return Paths.get(path).getFileName().toString();
        }
        return null;
    }

    @Value("${app.file.download-prefix}")
    private String downloadPrefix;

    @Named("generateUrl")
    public String generateUrl(String fileId) {
        if (fileId != null) {
            return downloadPrefix + fileId;
        }
        return null;
    }

    @Mapping(target = "originalFileName", source = "name")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "type", source = "contentType")
    public abstract FileResponse toFileResponse(FileInfo fileInfo);
}
