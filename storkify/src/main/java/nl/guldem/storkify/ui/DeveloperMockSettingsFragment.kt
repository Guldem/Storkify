package nl.guldem.storkify.ui

import android.content.Context
import android.os.Bundle
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import nl.guldem.BuildConfig

class DeveloperMockSettingsFragment: PreferenceFragmentCompat() {
//todo implementeren
    //private val developerMockSettingsViewModel: DeveloperMockSettingsViewModel by viewModels() //todo initiate viewmodel


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        //setup shared preferences
        preferenceManager.sharedPreferencesName = BuildConfig.LIBRARY_PACKAGE_NAME
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE

        val baseMockSettingsScreen = preferenceManager.createPreferenceScreen(context)


        val baseMockSettingsCategory = PreferenceCategory(context)
        baseMockSettingsCategory.title = "Base settings"

        //add use mock switch
        val useMock = SwitchPreferenceCompat(context)
        useMock.title = "Use mock"
        useMock.switchTextOn = "Turn mock on"
        useMock.switchTextOff = "Turn mock off"
        useMock.key = "useMock"


        //add use http fallback
        val useHTTPFallback = SwitchPreferenceCompat(context)
        useHTTPFallback.title = "Use HTTP fallback"
        useHTTPFallback.summary = "Normal request are done if there is no matching mock"
        useHTTPFallback.switchTextOn = "Turn HTTP fallback on"
        useHTTPFallback.switchTextOff = "Turn HTTP fallback off"
        useHTTPFallback.key = "useHTTPFallback"

        //add preferences to the view
        baseMockSettingsScreen.addPreference(baseMockSettingsCategory)
        baseMockSettingsCategory.addPreference(useMock)
        baseMockSettingsCategory.addPreference(useHTTPFallback)


        val mockArrayCategory = PreferenceCategory(context)
        mockArrayCategory.title = "Mock array"

        //add list of mockData
        val mockData = MultiSelectListPreference(context)
        mockData.key = "selectedMockData"
        mockData.title = "Select mockData to use"
        mockData.summary = ""

        val mockDataArray =
            emptyArray<String>()//todo implementeren //developerMockSettingsViewModel.getMockDataArray().toTypedArray()
        //set display labels for mock data
        mockData.entries = mockDataArray
        //set keys array for mock data
        mockData.entryValues = mockDataArray

        //select the first value if array has not been set
        //todo implementeren
//        if (!developerMockSettingsViewModel.mockDataStored()) {
//            mockData.values = setOf(mockDataArray.firstOrNull())
//        }

        baseMockSettingsScreen.addPreference(mockArrayCategory)
        mockArrayCategory.addPreference(mockData)

        preferenceScreen = baseMockSettingsScreen
    }
}