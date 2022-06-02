package com.example.imagecommanderback.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.example.imagecommanderback.dto.ImageDto;
import com.example.imagecommanderback.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service provides methods to operate with s3 object in bucket.
 *
 * @author Rodion Minkin
 */
@Service
public class S3ServiceImpl implements S3Service {

    private AmazonS3 s3Client;
    private TransferManager s3TransferManager;
    private String bucketName;

    public S3ServiceImpl(@Value("${cloud.aws.custom.bucket-name}") String bucketName,
                         @Value("${cloud.aws.region.static}") String region,
                         @Value("${cloud.aws.credentials.access-key}") String accessKey,
                         @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        this.bucketName = bucketName;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        this.s3TransferManager = TransferManagerBuilder.standard().withS3Client(s3Client).withMultipartUploadThreshold((long) (5 * 256 * 256)).build();
    }

    @Override
    public UploadResult putS3Object(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Provided file is empty");
        }
        File fileToUpload = null;
        try {
            fileToUpload = convertMultiPartFileToFile(multipartFile);
            return this.s3TransferManager.upload(this.bucketName, multipartFile.getOriginalFilename(), fileToUpload).waitForUploadResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileToUpload != null) {
                fileToUpload.delete();
            }
        }
    }


    @Override
    public ImageDto getS3ObjectContent(String keyName) {
        S3Object o = s3Client.getObject(this.bucketName, keyName);
        S3ObjectInputStream s3is = o.getObjectContent();
        try {
            return new ImageDto(keyName, s3is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public S3Object getS3Object(String keyName) {
        return s3Client.getObject(this.bucketName, keyName);
    }

    @Override
    public void deleteS3Object(String objectKey) {
        s3Client.deleteObject(this.bucketName, objectKey);
    }

    @Override
    public List<S3ObjectSummary> listS3Objects() {
        return this.s3Client.listObjects(bucketName).getObjectSummaries();
    }

    @Override
    public List<ImageDto> getS3Objects() {
        List<ImageDto> imageDtos = new ArrayList<>();
        List<S3ObjectSummary> objectSummaries = this.s3Client.listObjects(bucketName).getObjectSummaries();
        objectSummaries.stream().forEach(s3ObjectSummary -> {
            imageDtos.add(this.getS3ObjectContent(s3ObjectSummary.getKey()));
        });
        return imageDtos;
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) throws IOException {
        final File file = new File(multipartFile.getOriginalFilename());
        final FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        return file;
    }
}
