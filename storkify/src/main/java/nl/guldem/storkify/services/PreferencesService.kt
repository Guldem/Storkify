package nl.guldem.storkify.services

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//TODO: Make dynamic
const val PREFERENCE_NAME = "Storkify"

class PreferencesService(context: Context) {

    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    var useHTTPFallback by BooleanPreference()
    var useMock by BooleanPreference()
    var selectedMockData by NullableStringSetPreference()

    private class BooleanPreference : AbstractPreference<Boolean>(
        read = { key -> getBoolean(key, false) },
        write = { key, value -> putBoolean(key, value) }
    )

    private class NullableStringPreference : AbstractPreference<String?>(
        read = { key -> getString(key, null) },
        write = { key, value -> putString(key, value) }
    )

    private class NullableStringSetPreference : AbstractPreference<Set<String>?>(
        read = { key -> getStringSet(key, null) },
        write = { key, value -> putStringSet(key, value) }
    )

    private abstract class AbstractPreference<T>(
        private val read: SharedPreferences.(String) -> T,
        private val write: SharedPreferences.Editor.(String, T) -> Unit
    ) : ReadWriteProperty<PreferencesService, T> {
        final override fun getValue(thisRef: PreferencesService, property: KProperty<*>): T {
            return read.invoke(thisRef.prefs, property.name)
        }

        final override fun setValue(thisRef: PreferencesService, property: KProperty<*>, value: T) {
            if (value == null) {
                thisRef.prefs.edit { remove(property.name) }
            } else {
                thisRef.prefs.edit { write.invoke(this, property.name, value) }
            }
        }
    }
}

