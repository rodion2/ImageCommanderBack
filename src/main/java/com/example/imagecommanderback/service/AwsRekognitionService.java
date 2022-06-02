package com.example.imagecommanderback.service;

import java.util.List;

/**
 * Service provides methods to work with AWSRekognition cloud service.
 */
public interface AwsRekognitionService {

    /**
     * Returns labels of all found and recognised objects in s3 bucket object.
     *
     * @param nameObject provided object name.
     * @return List of recognised object names.
     */
    List<String> detectLabels(String nameObject);
}
