package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.Service.S3Service;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    private static final int MAX_URLS_PER_HOUR = 100; // Limit to 100 URLs per hour

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);


    // API endpoint to get presigned URL
    @GetMapping("/generate-presigned-url")
    public ResponseEntity<Map<String, Object>> generatePresignedUrl(HttpSession session, @RequestParam("fileName") String fileName, @RequestParam("maxSizeInBytes") long maxSizeInBytes) {
        if (!isValidFileName(fileName)) {
            throw new IllegalArgumentException("Invalid file name only alphanumeric characters, dots, underscores, and hyphens are allowed");
        }


//        String rateLimitKey = "presignedUrlRateLimit"; // Global rate limit key
//        long currentTimestamp = Instant.now().getEpochSecond();// Get current timestamp in seconds
//        String rateLimitKeyWithTimestamp = rateLimitKey + ":" + (currentTimestamp / 3600); // Use hourly window

//        // Increment the request counter in Redis
//        Long count = redisTemplate.opsForValue().increment(rateLimitKeyWithTimestamp, 1);
//
//        if (count == 1) {
//            // Set expiration for this key (1 hour) to reset the counter
//            redisTemplate.expire(rateLimitKeyWithTimestamp, 1, TimeUnit.HOURS);
//        }
//
//        if (count > MAX_URLS_PER_HOUR) {
//           // throw new IllegalArgumentException("Rate limit exceeded. Please try again later.");
//            Map<String, Object> response = new HashMap<>();
//            response.put("error", "Rate limit exceeded. Please try again later.");
//            // Return the response body with the map, and explicitly set the status code
//            return ResponseEntity
//                    .status(429)  // Set the HTTP status to 429
//                    .body(response);
//        }



        String sessionId = session.getId(); // Get session ID
        String tempDirectory =  sessionId + "/" + fileName; // Create a temporary directory with session ID

        logger.info("Generating presigned URL for file: " + fileName);

        URL presignedUrl = s3Service.generatePresignedUrl(tempDirectory, maxSizeInBytes);
        Map<String, Object> response = new HashMap<>();
        response.put("presignedUrl", presignedUrl.toString());
        response.put("tempDirectory", tempDirectory);
        // Return the map wrapped in a ResponseEntity with a 200 OK status
        return ResponseEntity
                .status(200)  // Optional: you can omit this as 200 is default
                .body(response);
    }

    private boolean isValidFileName(String fileName) {
        return fileName.matches("^[a-zA-Z0-9._-]+$");// Only allow alphanumeric characters, dots, underscores, and hyphens
    }
}
