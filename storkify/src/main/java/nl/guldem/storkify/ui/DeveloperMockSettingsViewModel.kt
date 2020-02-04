package nl.guldem.storkify.ui

import androidx.lifecycle.ViewModel
import nl.guldem.storkify.services.mock.MockService

class DeveloperMockSettingsViewModel constructor(
    private val mockService: MockService
) : ViewModel() {

    fun getMockDataArray(): List<String> {
        return mockService.getMockDataArray()
    }

    fun mockDataStored(): Boolean {
        return mockService.mockDataStored()
    }

}
