package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.FileUploadResponseDto;
import com.levdevs.freindshipbe.Service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reservationsFile")
public class ReservationFileController {

    private final ReservationService reservationService;

    private final Logger logger = LoggerFactory.getLogger(ReservationFileController.class);

    public ReservationFileController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(HttpSession session, @RequestPart("path") String inputPath, @RequestPart("file") MultipartFile file) {
        return uploadFileInternal(null, session, inputPath, file);
    }

    @PostMapping(path = "/{reservationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@PathVariable Long reservationId, @RequestPart("path") String inputPath, @RequestPart("file") MultipartFile file) {
        return uploadFileInternal(reservationId, null, inputPath, file);
    }

    //need crud for the files
    @GetMapping("/{reservationId}")
    public ResponseEntity<MultipartFile> getFiles(@PathVariable Long reservationId, @RequestParam(required = true) String fileName) {
        return ResponseEntity.ok(reservationService.getFile(reservationId, fileName));
    }

    @GetMapping("/{reservationId}/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@PathVariable Long reservationId, @RequestParam(required = true) String fileName) {
        return ResponseEntity.ok(reservationService.getPresignedUrl(reservationId, fileName));
    }

    private ResponseEntity<String> uploadFileInternal(Long reservationId, HttpSession session, String inputPath, MultipartFile file) {
        String path = inputPath.trim();

        logger.info("Received file: {}", file.getOriginalFilename());
        logger.info("File size: {}", file.getSize());
        logger.info("File type: {}", file.getContentType());
        logger.info("Path: {}", path);

        // Call your service to handle the file upload
        FileUploadResponseDto response;
        if (reservationId != null) {
            response = reservationService.uploadFile(reservationId, path, file, false);
        } else {
            response = reservationService.uploadFile(session, path, file);
        }

        logger.info("File uploaded successfully: {}", response);
        return ResponseEntity.ok("File uploaded successfully. " + response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long reservationId, @RequestParam(required = true) String fileName) {
        logger.info("Deleting file: {}", fileName);
        reservationService.deleteFile(reservationId, fileName);
        return ResponseEntity.ok("File deleted successfully.");
    }
}
