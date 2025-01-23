package com.levdevs.freindshipbe.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import java.io.IOException;
import java.time.Duration;
import java.net.URL;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    // Upload file to S3
    public String uploadFile(MultipartFile file, String key)  {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    public List<ObjectVersion> listObjectVersions( String key) {
        // Build the ListObjectVersionsRequest
        ListObjectVersionsRequest listObjectVersionsRequest = ListObjectVersionsRequest.builder()
                .bucket(bucketName)
                .prefix(key)  // Optional: Limit to the specific object key
                .build();

        // List all versions of the object
        ListObjectVersionsResponse response = s3Client.listObjectVersions(listObjectVersionsRequest);

        // Get the list of versions
        return response.versions();
    }

    public DeleteObjectResponse deleteFile(String key, String versionId) {
        // Build the DeleteObjectRequest
        DeleteObjectRequest.Builder deleteObjectRequestBuilder = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key);

        // If versionId is provided, delete the specific version
        if (versionId != null && !versionId.isEmpty()) {
            deleteObjectRequestBuilder.versionId(versionId); // Specify the version ID to delete
        }

        // Delete the object (or version)
        return s3Client.deleteObject(deleteObjectRequestBuilder.build());
    }



    public MultipartFile downloadFile(String key){
        return downloadFile(key, null);
    }

    public MultipartFile downloadFile(String key, String versionId) {
        try {
            GetObjectRequest getObjectRequest;
            if( versionId != null){
                getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .versionId(versionId)
                        .build();
            }else {
                // Build the GetObjectRequest
                getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
            }
            // Retrieve the file from S3
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            // Convert the InputStream to a MultipartFile
            MultipartFile multipartFile = new MockMultipartFile(
                    key,                                // File name
                    key,                                // Original file name
                    s3Object.response().contentType(), // Content type
                    s3Object                           // InputStream containing file data
            );

            return multipartFile;

        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    // Check if a file exists in the S3 bucket
    public boolean doesFileExist(String key) {
        try {
            // Send headObject request to S3 to check if the file exists
            //this will throw an exception if the file does not exist
            //this is a workaround to avoid using the listObjects API
            //this is using the headObject API which is more efficient for checking if a file exists in the bucket or not
            //then using the listObjects API which is more expensive and slower
            //what headObject does is it sends a request to S3 to check if the file exists and if it does it will return a 200 status code
            //headObject is also used to get metadata about the file
            // Check if the file exists in the S3 bucket
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)  // fileName could be something like 'patient' or 'guest0'
                    .build();

            HeadObjectResponse response = s3Client.headObject(headRequest);
            logger.info("Successful Response for checking if s3 file exists : " + response);

            // If the response does not throw an exception, file exists
            return true;
//            return s3Client.headObject(builder -> builder.bucket(bucketName).key(key))
//                    .sdkHttpResponse().isSuccessful();
        } catch (NoSuchKeyException e) {
            logger.info("File does not exist in S3 bucket exception: " + e);
            return false;
        } catch (Exception e) {
            logger.info("File S3 bucket exception other then NoSuchKeyException: " + e);
            logger.error("Error checking if file exists in S3 bucket", e);
            throw e;
        }
    }

    // Move a file within the S3 bucket
    public void moveFileWithinS3(String oldLocation, String newLocation) {
        try {
            // Copy file
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .copySource(bucketName + "/" + oldLocation)
                    .bucket(bucketName)
                    .key(newLocation)
                    .build();
            s3Client.copyObject(copyObjectRequest);

            // Delete original file
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(oldLocation)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            logger.error("Error moving file within S3 bucket", e);
            throw e;
        }

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

    // Generate a presigned URL for file download
    public String generatePresignedUrlForDownload( String filename) {
        try {
            // Create the GetObjectRequest
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            // Generate the Presigned URL
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
                    builder -> builder
                            .signatureDuration(Duration.ofMinutes(15)) // Set expiration
                            .getObjectRequest(getObjectRequest)
            );

            // Return the Presigned URL
            URL url = presignedRequest.url();
            return url.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating presigned URL: " + e.getMessage());
        }
    }

}
