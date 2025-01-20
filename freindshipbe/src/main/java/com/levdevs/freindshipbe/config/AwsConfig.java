package com.levdevs.freindshipbe.config;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;

    private S3Presigner s3Presigner;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        if (s3Presigner == null){
            s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        }
            return s3Presigner;
    }

    @PreDestroy
    public void closeS3Presigner() {
        if (s3Presigner != null) {
            s3Presigner.close();
        }
    }
}
// Compare this snippet from src/main/java/com/luv2code/springboot/cruddemo/security/DemoSecurityConfig.java:
// package com.luv2code.springboot.cruddemo.security;
//