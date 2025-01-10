package com.levdevs.freindshipbe.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    // Upload file to S3
    public String uploadFile(MultipartFile file, String key) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    // Move a file within the S3 bucket
    public void moveFileToProperLocation(String tempFileKey, String userId) {
        String targetKey = "users/" + userId + "/" + tempFileKey;

        // Copy file
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .copySource(bucketName + "/" + tempFileKey)
                .bucket(bucketName)
                .key(targetKey)
                .build();
        s3Client.copyObject(copyObjectRequest);

        // Delete original file
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(tempFileKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    // Generate a presigned URL for file upload
    public URL generatePresignedUrl(String fileName, long maxSizeInBytes) {
        // Validate file size
        if (maxSizeInBytes > 10 * 1024 * 1024) { // 10 MB
            throw new IllegalArgumentException("File size exceeds the maximum allowed size");
        }

        // Create a key (file path) by combining the upload path and file name
        String fileKey = "temp/" + fileName;

        // Create a presigned PUT request
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(15))
                        .putObjectRequest(putObjectRequest)
                        .build()
        );

        return presignedRequest.url();
    }
}
