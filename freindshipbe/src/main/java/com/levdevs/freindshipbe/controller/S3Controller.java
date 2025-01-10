package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.Service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);


    // API endpoint to get presigned URL
    @GetMapping("/generate-presigned-url")
    public URL generatePresignedUrl(@RequestParam("fileName") String fileName, @RequestParam("maxSizeInBytes") long maxSizeInBytes) {
        logger.info("Generating presigned URL for file: " + fileName);
        return s3Service.generatePresignedUrl(fileName, maxSizeInBytes);
    }
}
