package com.example.imagecommanderback.service;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.example.imagecommanderback.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service api to operate with s3 bucket objects.
 *
 * @author Rodion Minkin.
 */
public interface S3Service {

    /**
     * Puts provided file to s3 and returns result with metadata about loaded file.
     *
     * @param file - file to upload.
     * @return - upload result.
     * @throws IOException
     */
    UploadResult putS3Object(MultipartFile file);

    /**
     * Gets s3ObjectContent from s3 bucket.
     *
     * @param keyName - file keyname.
     * @return dto with fileName and content.
     */
    ImageDto getS3ObjectContent(String keyName);

    /**
     * Returns s3Object from s3Bucket by name.
     *
     * @param keyName - file name.
     * @return object from s3 bucket.
     */
    S3Object getS3Object(String keyName);

    /**
     * Deletes s3 object from s3 bucket by objectKey.
     *
     * @param objectKey
     */
    void deleteS3Object(String objectKey);

    /**
     * Returns list of s3 objects summaries from s3 bucket.
     * @return  list of s3 objects summaries.
     */
    List<S3ObjectSummary> listS3Objects();

    /**
     * Returns list of all s3 objects from s3 bucket.
     *
     * @return list of all s3 objects from s3 bucket.
     */
    List<ImageDto> getS3Objects();
}
