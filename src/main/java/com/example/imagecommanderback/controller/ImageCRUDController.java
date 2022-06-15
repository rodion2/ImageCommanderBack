package com.example.imagecommanderback.controller;

import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.example.imagecommanderback.dto.ImageDto;
import com.example.imagecommanderback.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller for CRUD operations with s3Bucket objects.
 *
 * @author Rodion Minkin
 */
@RestController
@RequestMapping(value = "/object")
public class ImageCRUDController {
    private S3Service s3service;

    public ImageCRUDController(@Autowired S3Service s3service) {
        this.s3service = s3service;
    }

    @GetMapping(path = "/list")
    public List<ImageDto> listObjects() {
        return this.s3service.getS3Objects();
    }

    @GetMapping
    public ImageDto getObject(@RequestParam(value = "object-name") String objectName) {
        return this.s3service.getS3ObjectContent(objectName);
    }

    @PostMapping(path = "/upload")
    public UploadResult uploadObject(@RequestParam(value = "file") MultipartFile file) {
        return this.s3service.putS3Object(file);
    }

    @DeleteMapping(value = "/{objectName}")
    public void deleteObject(@PathVariable String objectName) {
        s3service.deleteS3Object(objectName);
    }
}
