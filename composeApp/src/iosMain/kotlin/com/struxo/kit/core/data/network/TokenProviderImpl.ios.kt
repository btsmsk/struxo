package com.struxo.kit.core.data.network

import platform.Foundation.NSUserDefaults

/**
 * iOS [TokenProvider] backed by [NSUserDefaults].
 *
 * Stores access and refresh tokens in standard user defaults.
 *
 * **Note:** For production use, upgrade to Keychain storage
 * for secure credential persistence.
 */
class TokenProviderImpl : TokenProvider {

    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    override suspend fun getTokens(): Pair<String, String>? {
        val access = defaults.stringForKey(KEY_ACCESS_TOKEN) ?: return null
        val refresh = defaults.stringForKey(KEY_REFRESH_TOKEN) ?: return null
        return access to refresh
    }

    override suspend fun refreshTokens(): Pair<String, String>? {
        // Token refresh is handled by the Ktor Auth plugin via ApiClient.
        // This returns stored tokens so the plugin can attempt a refresh call.
        return getTokens()
    }

    override suspend fun saveTokens(access: String, refresh: String) {
        defaults.setObject(access, forKey = KEY_ACCESS_TOKEN)
        defaults.setObject(refresh, forKey = KEY_REFRESH_TOKEN)
    }

    override suspend fun clearTokens() {
        defaults.removeObjectForKey(KEY_ACCESS_TOKEN)
        defaults.removeObjectForKey(KEY_REFRESH_TOKEN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
