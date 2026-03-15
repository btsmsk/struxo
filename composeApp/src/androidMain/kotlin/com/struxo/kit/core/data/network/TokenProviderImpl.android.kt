package com.struxo.kit.core.data.network

import android.content.Context
import android.content.SharedPreferences

/**
 * Android [TokenProvider] backed by [SharedPreferences].
 *
 * Stores access and refresh tokens in a private preferences file.
 *
 * **Note:** For production use, upgrade to `EncryptedSharedPreferences`
 * from the `androidx.security:security-crypto` library.
 *
 * @param context Application context.
 */
class TokenProviderImpl(context: Context) : TokenProvider {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getTokens(): Pair<String, String>? {
        val access = prefs.getString(KEY_ACCESS_TOKEN, null) ?: return null
        val refresh = prefs.getString(KEY_REFRESH_TOKEN, null) ?: return null
        return access to refresh
    }

    override suspend fun refreshTokens(): Pair<String, String>? {
        // Token refresh is handled by the Ktor Auth plugin via ApiClient.
        // This returns stored tokens so the plugin can attempt a refresh call.
        return getTokens()
    }

    override suspend fun saveTokens(access: String, refresh: String) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, access)
            .putString(KEY_REFRESH_TOKEN, refresh)
            .apply()
    }

    override suspend fun clearTokens() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    private companion object {
        const val PREFS_NAME = "struxo_tokens"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
