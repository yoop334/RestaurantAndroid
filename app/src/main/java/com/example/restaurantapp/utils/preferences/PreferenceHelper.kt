package com.example.restaurantapp.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(
    context: Context
) {
    private val preferences: SharedPreferences = context.getSharedPreferences(CONTEXT_NAME, Context.MODE_PRIVATE)

    fun setToken(token: String) {
        preferences[TOKEN] = token
    }

    fun getToken(): String {
        return preferences[TOKEN] ?: ""
    }

    fun setRefreshToken(token: String) {
        preferences[REFRESH_TOKEN] = token
    }

    fun getRefreshToken(): String {
        return preferences[REFRESH_TOKEN] ?: ""
    }

    fun setUserId(userId: Long) {
        preferences[USER_ID] = userId
    }

    fun getUserId(): Long {
        return preferences[USER_ID] ?: 0
    }

    fun clearAll() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val CONTEXT_NAME = "SharedPreferences"
        private const val TOKEN = "token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
    }
}


/**
 * SharedPreferences extension function, to listen the edit() and apply() fun calls
 * on every SharedPreferences operation.
 */
private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}
/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
private operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
private inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
