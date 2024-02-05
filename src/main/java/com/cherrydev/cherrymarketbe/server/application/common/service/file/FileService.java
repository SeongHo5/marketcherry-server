package com.cherrydev.cherrymarketbe.server.application.common.service.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.ServiceFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3Client objectStorageClient;
    private final FileValidator fileValidator;

    public static final String BUCKET_NAME = "cherry-resource";
    protected static final String[] SUPPORTED_IMAGE_FORMAT = {"jpg", "jpeg", "png"};
    public static final int FILE_LIMIT_MAX_COUNT = 3;
    public static final long FILE_LIMIT_MAX_SIZE = 3L * 1024 * 1024; // 3MB
    public static final String DIRECTORY_SEPARATOR = "/";

    /**
     * 단일 파일 업로드
     *
     * @param multipartFile 업로드할 파일(이미지만 가능)
     * @param dirName       업로드할 디렉토리 이름
     */
    public String uploadSingleFile(MultipartFile multipartFile, String dirName) {
        fileValidator.validateBeforeUploadSignleFile(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        multipartFile.getOriginalFilename();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String fileName = dirName + DIRECTORY_SEPARATOR + multipartFile.getOriginalFilename();
        try {
            return putFileToBucket(multipartFile.getInputStream(), fileName, objectMetadata);
        } catch (IOException e) {
            throw new ServiceFailedException(FAILED_TO_UPLOAD_FILE);
        }
    }

    /**
     * 다중 파일 업로드
     *
     * @param multipartFiles 업로드할 파일(이미지만 가능)
     * @param dirName        업로드할 디렉토리 이름
     */
    public void uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) {

        fileValidator.validateBeforeUploadMultipleFiles(multipartFiles);

        ObjectMetadata objectMetadata = new ObjectMetadata();

        for (MultipartFile multipartFile : multipartFiles) {
            objectMetadata.setContentLength(multipartFile.getSize());

            String fileName = dirName + DIRECTORY_SEPARATOR + multipartFile.getOriginalFilename();
            try {
                log.info("Upload Image to Object Storage : " + fileName);
                putFileToBucket(multipartFile.getInputStream(), fileName, objectMetadata);
            } catch (IOException e) {
                throw new ServiceFailedException(FAILED_TO_UPLOAD_FILE);
            }
        }
    }

    /**
     * 단일 파일 삭제
     *
     * @param url     파일 URL
     * @param dirName 삭제할 파일이 있는 디렉토리 이름
     */
    public void deleteSingleFile(String url, String dirName) {
        fileValidator.checkFileExist(url);
        deleteFileFromBucket(url, dirName);
    }

    /**
     * 다중 파일 삭제
     *
     * @param urls    파일 URL 목록
     * @param dirName 삭제할 파일이 있는 디렉토리 이름
     */
    public void deleteMultipleFiles(List<String> urls, String dirName) {
        urls.forEach(url -> deleteFileFromBucket(url, dirName));
    }

    // ============== PRIVATE METHODS ==============

    /**
     * Object Storage 파일 업로드 처리
     */
    private String putFileToBucket(InputStream file, String fileName, ObjectMetadata objectMetadata) {
        objectStorageClient.putObject(
                new PutObjectRequest(BUCKET_NAME, fileName, file, objectMetadata).withCannedAcl(
                        CannedAccessControlList.PublicRead));
        return objectStorageClient.getUrl(BUCKET_NAME, fileName).toString();
    }

    /**
     * Object Storage 파일 삭제 처리
     */
    private void deleteFileFromBucket(String url, String dirName) {
        final String[] split = url.split("/");
        final String fileName = dirName + DIRECTORY_SEPARATOR + split[split.length - 1];
        DeleteObjectRequest request = new DeleteObjectRequest(BUCKET_NAME, fileName);
        log.info("Deleted Image from Object Storage : " + request);
        objectStorageClient.deleteObject(request);
    }

}
