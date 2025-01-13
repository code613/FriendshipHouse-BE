package com.levdevs.freindshipbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.boot.Metadata;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleFileUpload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("guestFiles") List<MultipartFile> guestFiles,
            @RequestPart("metadata") String metadata) {

        // Process the file
        String fileName = file.getOriginalFilename();
        System.out.println("LOGGING");
        System.out.println(metadata);
        System.out.println(fileName);

        // Parse the metadata JSON if needed
        // Example: convert JSON string to a Java object
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Metadata metadataObj = objectMapper.readValue(metadata, Metadata.class);
//            // Handle metadata object
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid metadata JSON");
//        }

        return ResponseEntity.ok("File: " + fileName + " uploaded successfully with metadata.");
    }
}
