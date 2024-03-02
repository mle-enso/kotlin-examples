package de.mle.kotlin

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.s3.S3Client

class LambdaFunctionTest {
    private val lambdaFunction: LambdaFunction = LambdaFunction()
    private val s3Client: S3Client = mockk(relaxed = true)

    @Test
    fun doSomething() {
        // when
        val result = lambdaFunction.doSomething(s3Client).invoke("example value")

        // then
        assertThat(result).isEqualTo("Started Lambda function with header 'example value'")
    }
}