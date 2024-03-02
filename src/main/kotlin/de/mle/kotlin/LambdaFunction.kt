package de.mle.kotlin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Configuration
class LambdaFunction {
    @Bean
    fun refresh(s3Client: S3Client): (String) -> String {
        return {
            val requestBody = RequestBody.fromBytes(it.toByteArray())
            s3Client.putObject ( PutObjectRequest.builder().bucket("mybucket").key("object-key").build(), requestBody)
            "Started Lambda function with header '$it'"
        }
    }
}
