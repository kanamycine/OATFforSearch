package com.team6.onandthefarm.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Upload {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String profileUserUpload(MultipartFile multipartFile) throws IOException {
        String serverStr = "https://lotte-06-s3-test.s3.ap-northeast-2.amazonaws.com";
        String prefix = "profile/user/"+UUID.randomUUID() + "-";
        String s3FileName = prefix + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        String originStr = serverStr+s3FileName;

        return originStr;
    }

    public String profileSellerUpload(MultipartFile multipartFile) throws IOException {
        String s3FileName = "profile/seller/"+UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public String productUpload(MultipartFile multipartFile) throws IOException {
        String serverStr = "https://lotte-06-s3-test.s3.ap-northeast-2.amazonaws.com";
        String prefix = "product/"+UUID.randomUUID() + "-";
        String s3FileName = prefix + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        String originStr = serverStr+s3FileName;

        return originStr;
    }

    public String feedUpload(MultipartFile multipartFile) throws IOException {
        String s3FileName = "feed/"+UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
}