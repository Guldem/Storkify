package nl.guldem.storkify.services.mock

import com.squareup.moshi.JsonClass


/**
 * JSON Data types for Mock
 * **/
@JsonClass(generateAdapter = true)
data class MockMetadata(
    val request: MockRequest,
    val response: MockResponse
)

@JsonClass(generateAdapter = true)
data class MockRequest(
    val method: String,
    val path: String,
    val queryParameters: Map<String, String>?
)

@JsonClass(generateAdapter = true)
data class MockResponse(
    val code: Int,
    val responseHeaders: Map<String, String>?
)

/**
 * Mock class for request interceptor
 * **/
@JsonClass(generateAdapter = true)
data class MockData(
    val mockMetadata: MockMetadata,
    val pathResponseJson: String,
    val mockName: String
)

@JsonClass(generateAdapter = true)
data class MockError(
    val mockResponse: MockResponse,
    val jsonBody: String
)

sealed class MockResult {
    data class Data(val mockdata: MockData): MockResult()
    data class Error(val mockError: MockError): MockResult()

    val responseCode: Int
        get() = when (this) {
            is Data -> this.mockdata.mockMetadata.response.code
            is Error -> this.mockError.mockResponse.code
        }
}
