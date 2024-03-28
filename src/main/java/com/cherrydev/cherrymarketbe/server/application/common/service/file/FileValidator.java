package com.cherrydev.cherrymarketbe.server.application.common.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.server.application.common.service.file.FileService.*;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private final AmazonS3Client objectStorageClient;

    protected void validateBeforeUploadSignleFile(MultipartFile multipartFile) {
        checkFileExist(multipartFile);
        checkFileFormat(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        checkFileSizeLimit(multipartFile.getSize());
    }

    protected void validateBeforeUploadMultipleFiles(List<MultipartFile> multipartFiles) {
        checkFileExist(multipartFiles);
        checkFileCountLimit(multipartFiles.size());
        multipartFiles.forEach(multipartFile -> {
            checkFileFormat(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            checkFileSizeLimit(multipartFile.getSize());
        });
    }

    /**
     * 단일 파일 업로드 시 파일 null 체크
     */
    protected void checkFileExist(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ServiceFailedException(NO_FILE_TO_UPLOAD);
        }
    }

    /**
     * 다중 파일 업로드 시 파일 null 체크
     */
    private void checkFileExist(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null
                || multipartFiles.isEmpty()
                || multipartFiles.stream().anyMatch(file -> file == null || file.isEmpty())) {
            throw new ServiceFailedException(NO_FILE_TO_UPLOAD);
        }
    }

    /**
     * 파일 삭제 시 파일 존재 여부 체크
     */
    protected void checkFileExist(String url) {
        boolean isExist = objectStorageClient.doesObjectExist(BUCKET_NAME, url);
        if (!isExist) {
            throw new ServiceFailedException(NOT_FOUND_FILE);
        }
    }

    /**
     * 단일 파일 형식(확장자) 체크
     */
    protected void checkFileFormat(String fileName) {
        int index = fileName.lastIndexOf(".");
        String extension = fileName.substring(index + 1).toLowerCase();
        boolean isSupported = Arrays.asList(SUPPORTED_IMAGE_FORMAT).contains(extension);
        if (!isSupported) {
            throw new ServiceFailedException(UNSUPPORTED_FILE_FORMAT);
        }
    }

    /**
     * 1회 업로드 가능한 파일 개수 체크
     */
    protected void checkFileCountLimit(int fileCounts) {
        if (fileCounts > FILE_LIMIT_MAX_COUNT) {
            throw new ServiceFailedException(TOO_MANY_FILES);
        }
    }

    /**
     * 업로드 가능한 파일 용량 체크
     */
    protected void checkFileSizeLimit(long fileSize) {
        if (fileSize > FILE_LIMIT_MAX_SIZE) {
            throw new ServiceFailedException(FILE_TOO_LARGE);
        }
    }

}
