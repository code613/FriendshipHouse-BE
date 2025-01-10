package com.levdevs.freindshipbe.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private S3Service s3Service;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadFile() throws IOException {
        // Setup mock behavior
        String fileName = "test.txt";
        String fileContent = "Hello, AWS!";
        byte[] fileBytes = fileContent.getBytes();

        when(file.getBytes()).thenReturn(fileBytes);
        when(file.getContentType()).thenReturn("text/plain");

        PutObjectResponse putObjectResponse = PutObjectResponse.builder().build();
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(putObjectResponse);

        // Test the uploadFile method
        String fileUrl = s3Service.uploadFile(file, fileName);

        // Verify the behavior and assert the results
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assert fileUrl.equals("https://" + bucketName + ".s3.amazonaws.com/" + fileName);
    }

    @Test
    public void testMoveFileToProperLocation() {
        // Setup mock behavior
        String tempFileKey = "tempFile.txt";
        String userId = "user123";
        CopyObjectResponse copyObjectResponse = CopyObjectResponse.builder().build();
        when(s3Client.copyObject(any(CopyObjectRequest.class)))
                .thenReturn(copyObjectResponse);
        DeleteObjectResponse deleteObjectResponse = DeleteObjectResponse.builder().build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(deleteObjectResponse);

        // Test the moveFileToProperLocation method
        s3Service.moveFileToProperLocation(tempFileKey, userId);

        // Verify the behavior
        verify(s3Client, times(1)).copyObject(any(CopyObjectRequest.class));
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    public void testGeneratePresignedUrl() {
        // Setup mock behavior
        String fileName = "test.txt";
        long maxSizeInBytes = 5 * 1024 * 1024;  // 5MB
        URL presignedUrl = mock(URL.class);
        PresignedPutObjectRequest presignedPutObjectRequest = mock(PresignedPutObjectRequest.class);
        when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(presignedPutObjectRequest);
        when(presignedPutObjectRequest.url()).thenReturn(presignedUrl);

        // Test the generatePresignedUrl method
        URL result = s3Service.generatePresignedUrl(fileName, maxSizeInBytes);

        // Verify the behavior and assert the result
        verify(s3Presigner, times(1)).presignPutObject(any(PutObjectPresignRequest.class));
        assert result == presignedUrl;
    }
}
