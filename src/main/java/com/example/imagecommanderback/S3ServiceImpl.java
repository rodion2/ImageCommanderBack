package com.example.imagecommanderback;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sagemaker.model.CognitoConfig;

public class S3ServiceImpl implements S3Service {

    @Override
    public void putObject() {
        AWSCredentials creds = new AnonymousAWSCredentials();
        AmazonS3 s3 = new AmazonS3Client(creds);
    }

}
