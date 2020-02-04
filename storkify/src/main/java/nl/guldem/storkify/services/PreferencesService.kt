package nl.guldem.storkify.services

import android.content.Context

const val PREFERENCE_NAME = "Storkify"


//todo  implement preference service
class PreferencesService(context: Context) {

    var useHTTPFallback: Boolean = true
    var useMock: Boolean = true
    var selectedMockData: Set<String>? = null
}
