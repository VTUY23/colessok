package com.colessok.api.file.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.file.dto.FileInfo;
import com.colessok.api.file.entity.File;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class FileRepository {
    @Value("${app.file.storage-dir}")
    String storageDir;

    @Value("${app.file.download-prefix}")
    String urlPrefix;

    public FileInfo store(MultipartFile file) throws IOException {
        Path folder = Paths.get(storageDir);

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String fileName =
                Objects.isNull(fileExtension) ? UUID.randomUUID().toString() : UUID.randomUUID() + "." + fileExtension;

        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder()
                .name(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
    }

    public FileInfo getInfo(File file) throws IOException {
        // Kiểm tra nếu file là null
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        // Đảm bảo rằng tệp tồn tại trong hệ thống nếu sử dụng file path
        Path filePath = Paths.get(file.getPath()).normalize().toAbsolutePath();

        if (!Files.exists(filePath)) {
            throw new IOException("File not found at path: " + filePath);
        }

        // Lấy tên tệp từ đường dẫn
        String fileName = filePath.getFileName().toString();

        // Lấy phần mở rộng tệp nếu cần, ví dụ: .jpg, .png
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        // Nếu cần, có thể tạo lại tên tệp dựa trên extension hoặc giữ nguyên
        fileName = Objects.isNull(fileExtension) ? UUID.randomUUID().toString() : fileName;

        // Tạo URL từ tiền tố + tên tệp
        String fileUrl = urlPrefix + file.getId();

        // Trả về đối tượng FileInfo
        return FileInfo.builder()
                .name(fileName) // Tên tệp từ filePath
                .size(file.getSize()) // Kích thước tệp
                .contentType(file.getContentType()) // Kiểu nội dung của tệp
                .md5Checksum(file.getMd5Checksum()) // MD5 checksum
                .path(filePath.toString()) // Đường dẫn tệp
                .url(fileUrl) // URL tải tệp
                .build();
    }

    public Resource read(File file) throws IOException {
        var data = Files.readAllBytes(Path.of(file.getPath()));
        return new ByteArrayResource(data);
    }
}
