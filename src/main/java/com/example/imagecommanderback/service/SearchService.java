package com.example.imagecommanderback.service;

import com.example.imagecommanderback.dto.ImageDto;

import java.util.List;

/**
 * Service provides mithods to search image by containing name object.
 */
public interface SearchService {

    /**
     * Returns list of images from s3 bucket that contains provided object.
     *
     * @param objectName name of object that image should contain.
     * @return list of found images.
     */
    List<ImageDto> getImagesByLabel(String objectName);
}
