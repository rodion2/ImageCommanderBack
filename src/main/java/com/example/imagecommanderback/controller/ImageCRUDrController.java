package com.example.imagecommanderback.controller;

import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.example.imagecommanderback.dto.ImageDto;
import com.example.imagecommanderback.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
public class ImageCRUDrController {
    private S3Service s3service;

    public ImageCRUDrController(@Autowired S3Service s3service) {
        this.s3service = s3service;
    }

    @GetMapping(path = "/list")
    @ResponseBody
    public List<ImageDto> listObjects() {
        return this.s3service.getS3Objects();
    }

    @GetMapping
    @ResponseBody
    public ImageDto getObject(@RequestParam(value = "object-key") String objectKey) {
        return this.s3service.getS3ObjectContent(objectKey);
    }

    @PostMapping(path = "/upload")
    public UploadResult uploadObject(@RequestParam(value = "file") MultipartFile file) throws IOException {
        return this.s3service.putS3Object(file);
    }

    @DeleteMapping(value = "/{objectKey}")
    public void deleteObject(@PathVariable(value = "objectKey")  String objectKey) {
        s3service.deleteS3Object(objectKey);
    }
}
