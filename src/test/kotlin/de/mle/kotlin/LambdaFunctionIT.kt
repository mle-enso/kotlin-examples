package de.mle.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class LambdaFunctionIT(
        @Autowired private val lambdaFunction: LambdaFunction,
        @Autowired private val s3Client: S3Client
) {
    companion object {
        @Container
        val localStack = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))

        @JvmStatic
        @DynamicPropertySource
        @Throws(Exception::class)
        fun setContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.cloud.aws.s3.endpoint") { localStack.getEndpointOverride(S3) }
            registry.add("spring.cloud.aws.credentials.access-key", localStack::getAccessKey)
            registry.add("spring.cloud.aws.credentials.secret-key", localStack::getSecretKey)
            localStack.start()
            localStack.execInContainer("awslocal", "s3api", "create-bucket", "--bucket", "mybucket")
        }
    }

    @Test
    fun refresh() {
        // given
        val list = s3Client.listBuckets()
        assertThat(list.buckets().first().name()).isEqualTo("mybucket")

        // when
        val result = lambdaFunction.doSomething(s3Client)("input value")
        assertThat(result).isEqualTo("Started Lambda function with header 'input value'")

        // then
        val getRequest = GetObjectRequest.builder().bucket("mybucket").key("object-key").build()
        val s3Content = String(s3Client.getObject(getRequest).readAllBytes())
        assertThat(s3Content).isEqualTo("input value")
    }
}