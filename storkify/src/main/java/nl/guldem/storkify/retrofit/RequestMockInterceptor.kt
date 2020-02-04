package nl.guldem.storkify.retrofit

import android.content.Context
import nl.guldem.storkify.services.mock.MockResult
import nl.guldem.storkify.services.mock.MockService
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.buffer
import okio.source

class RequestMockInterceptor constructor(
    val context: Context,
    private val mockService: MockService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return mockService.checkForAvailableMockDataWithResponse(request)?.let { result ->

            val responseHeaders = Headers.headersOf().newBuilder()
            val responseBody = when (result) {
                is MockResult.Data -> {
                    //add all response headers. Fallback on application/json if empty
                    result.mockdata.mockMetadata.response.responseHeaders?.forEach { header ->
                        responseHeaders.add(header.key, header.value)
                    } ?: responseHeaders.add("content-type", "application/json")

                    val responseStream = context.assets.open(result.mockdata.pathResponseJson)
                    responseStream.source().buffer().asResponseBody()
                }
                is MockResult.Error -> {
                    responseHeaders.add("content-type", "application/json")
                    result.mockError.jsonBody.toResponseBody()
                }
            }

            chain.proceed(chain.request()).newBuilder()
                .code(result.responseCode)
                .protocol(Protocol.HTTP_2)
                .body(responseBody)
                .headers(responseHeaders.build())
                .build()
        } ?: chain.proceed(request)
    }
}