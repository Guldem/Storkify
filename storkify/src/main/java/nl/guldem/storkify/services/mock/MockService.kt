package nl.guldem.storkify.services.mock

import android.content.Context
import com.squareup.moshi.Moshi
import nl.guldem.storkify.services.PreferencesService
import okhttp3.Request

private const val MOCK_FOLDER_PATH = "mock"
private const val MOCK_METADATA_NAME = "metadata"
private const val MOCK_RESPONSE_NAME = "response"

class MockService constructor(val context: Context, val moshi: Moshi, private val preferencesService: PreferencesService) {

    private var mockData = fetchAllAvailableMockData()

    fun mockDataStored(): Boolean {
        return preferencesService.selectedMockData != null
    }

    fun getMockDataArray(): List<String> {
        return mockData.map { it.mockName }
    }

    private fun fetchAllAvailableMockData(): List<MockData> {
        val mockFiles = context.resources.assets.list(MOCK_FOLDER_PATH)
        val metadataFiles = mockFiles?.filter { it.contains(MOCK_METADATA_NAME) }

        val mockData: MutableList<MockData> = mutableListOf()
        metadataFiles?.forEach { metadataFileName ->
            try {
                val inputStream = context.assets.open("$MOCK_FOLDER_PATH/$metadataFileName")
                val json = inputStream.bufferedReader().use { it.readText() }
                moshi.adapter(MockMetadata::class.java).failOnUnknown().fromJson(json)?.let {
                    mockData.add(MockData(it, "$MOCK_FOLDER_PATH/${metadataFileName.replace(
                        MOCK_METADATA_NAME, MOCK_RESPONSE_NAME
                    )}", metadataFileName.replace("-$MOCK_METADATA_NAME.json", "")))
                }
            } catch (e: Exception) {
                // Tolbaaken.error(e) { "Error reading mock data: $metadataFileName" } todo add log tool
            }
        }

        //set first mock as selected if not previously set
        if (mockData.size > 0 && preferencesService.selectedMockData == null) {
            preferencesService.selectedMockData = setOf(mockData[0].mockName)
        }
        return mockData
    }

    /**
     * Check if the request is identical to a mock request
     * The path and query parameters are used for the check
     * **/
    fun checkForAvailableMockDataWithResponse(currentRequest: Request): MockResult? {
        return if (preferencesService.useMock) {
            //find matching request
            val metadata = mockData.find {
                var queryParametersValid = true

                it.mockMetadata.request.queryParameters?.forEach { currentQuery ->
                    val queryString = currentQuery.key + "=" + currentQuery.value
                    if (!currentRequest.url.toString().contains(queryString, true)) {
                        queryParametersValid = false
                    }
                }

                currentRequest.url.toString().contains(it.mockMetadata.request.path, true) && queryParametersValid && currentRequest.method.equals(
                    it.mockMetadata.request.method,
                    true
                ) && preferencesService.selectedMockData?.contains(it.mockName) ?: false
            }?.let {
                MockResult.Data(it)
            }

            if (!preferencesService.useHTTPFallback && metadata == null) {
                //val errorBody = context.resources.openRawResource(R.raw.mock_error).source().buffer().readUtf8() todo add error
                MockResult.Error(MockError(MockResponse((500), null), "asd")) //errorBody)) todo add error
            } else {
                metadata
            }
        } else {
            null
        }
    }
}
