package com.cherrydev.cherrymarketbe.server.application.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 네이버 클라우드 Object Storage 접근을 위한 Configuration
 *
 * <p>Object Storage도 AWS S3과 동일한 API로 접근 가능하다.
 */
@Configuration
public class NCPStorageConfig {

  @Value("${ncp.storage.region}")
  private String region;

  @Value("${ncp.storage.endpoint}")
  private String endPoint;

  @Value("${ncp.storage.accessKey}")
  private String accessKey;

  @Value("${ncp.storage.secretKey}")
  private String secretKey;

  @Bean
  public AmazonS3Client objectStorageClient() {
    return (AmazonS3Client)
        AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();
  }
}
