package com.teamolha.talantino.general.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class S3Config {
    @Value("${aws.bucket.access-key}")
    private String ACCESS_KEY;

    @Value("${aws.bucket.secret-key}")
    private String SECRET_KEY;

    @Value("${aws.bucket.region}")
    private String BUCKET_REGION;

    @Bean
    public AmazonS3 S3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                ACCESS_KEY,
                SECRET_KEY
        );
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(BUCKET_REGION)
                .build();
    }
}
