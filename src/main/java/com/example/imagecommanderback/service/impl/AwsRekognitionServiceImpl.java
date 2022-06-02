package com.example.imagecommanderback.service.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.example.imagecommanderback.service.AwsRekognitionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsRekognitionServiceImpl implements AwsRekognitionService {

    private AmazonRekognition rekognitionClient;
    private String bucketName;

    public AwsRekognitionServiceImpl(@Value("${cloud.aws.custom.bucket-name}") String bucketName,
                                     @Value("${cloud.aws.region.static}") String region,
                                     @Value("${cloud.aws.credentials.access-key}") String accessKey,
                                     @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        this.bucketName = bucketName;
        this.rekognitionClient = createClient(region, bucketName, accessKey, secretKey);
    }

    @Override
    public List<String> detectLabels(String nameObject) {
        DetectLabelsRequest detectLabelsRequest = new DetectLabelsRequest()
                .withMaxLabels(10)
                .withImage(new Image().withS3Object(new S3Object().withName(nameObject).withBucket(this.bucketName)));
        return this.rekognitionClient.detectLabels(detectLabelsRequest).getLabels().stream()
                .map(label -> label.getName().toLowerCase()).collect(Collectors.toList());
    }

    private AmazonRekognition createClient(String region, String bucketName, String accessKey, String secretKey) {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(30000);
        clientConfig.setRequestTimeout(60000);
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonRekognitionClientBuilder
                .standard()
                .withClientConfiguration(clientConfig)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(region)
                .build();
    }
}
