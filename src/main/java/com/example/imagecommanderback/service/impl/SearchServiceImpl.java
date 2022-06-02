package com.example.imagecommanderback.service.impl;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.imagecommanderback.dto.ImageDto;
import com.example.imagecommanderback.service.AwsRekognitionService;
import com.example.imagecommanderback.service.S3Service;
import com.example.imagecommanderback.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for SearchService
 *
 * @author Rodion Minkin.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private AwsRekognitionService awsRekognitionService;
    private S3Service s3Service;

    public SearchServiceImpl(@Autowired AwsRekognitionService awsRekognitionService,
                             @Autowired S3Service s3Service) {
        this.awsRekognitionService = awsRekognitionService;
        this.s3Service = s3Service;
    }

    @Override
    public List<ImageDto> getImagesByLabel(String objectName) {
        List<S3ObjectSummary> s3ObjectSummaries = s3Service.listS3Objects();
        List<ImageDto> imageDtos = new ArrayList<>();
        List<S3ObjectSummary> s3ObjectSummariesWithSearchedLabel = s3ObjectSummaries.stream()
                .filter(s3ObjectSummary ->
                        awsRekognitionService
                                .detectLabels(s3ObjectSummary.getKey())
                                .contains(objectName.toLowerCase()))
                .collect(Collectors.toList());
        s3ObjectSummariesWithSearchedLabel.forEach(s3ObjectSummary -> {
            ImageDto dto = new ImageDto(s3ObjectSummary.getKey(),
                    s3Service.getS3ObjectContent(s3ObjectSummary.getKey()).getContent());

            imageDtos.add(dto);
        });
        return imageDtos;
    }
}
