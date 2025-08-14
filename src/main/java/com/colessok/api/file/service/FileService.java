package com.colessok.api.file.service;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.exception.AppException;
import com.colessok.api.common.exception.ErrorCode;
import com.colessok.api.file.dto.response.FileData;
import com.colessok.api.file.dto.response.FileResponse;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.mapper.FileMapper;
import com.colessok.api.file.repository.FileMgmtRepository;
import com.colessok.api.file.repository.FileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileService {
    FileRepository fileRepository;
    FileMgmtRepository fileMgmtRepository;

    FileMapper fileMgmtMapper;

    public FileResponse uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getSize() <= 0) throw new AppException(ErrorCode.FILE_EMPTY);

        var fileInfo = fileRepository.store(file);

        var fileMgmt = fileMgmtMapper.toFile(fileInfo);
        String userId = "anonymous"; // Hoặc null, hoặc lấy từ context nếu có
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        fileMgmt = fileMgmtRepository.save(fileMgmt);

        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getUrl())
                .type(file.getContentType())
                .build();
    }

    public File uploadAndSave(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new AppException(ErrorCode.FILE_EMPTY);

        var fileInfo = fileRepository.store(file);
        var fileEntity = fileMgmtMapper.toFile(fileInfo);

        return fileMgmtRepository.save(fileEntity);
    }

    public FileResponse getFileInfo(File file) throws IOException {
        var fileInfo = fileRepository.getInfo(file);
        return FileResponse.builder()
                .originalFileName(fileInfo.getName())
                .url(fileInfo.getUrl())
                .build();
    }

    public FileData download(String fileName) throws IOException {
        var fileMgmt =
                fileMgmtRepository.findById(fileName).orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        var resource = fileRepository.read(fileMgmt);

        return new FileData(fileMgmt.getContentType(), resource);
    }
}
