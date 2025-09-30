package com.example.firebaseremoteconfig

import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

open class RemoteConfigManager(private val context: Context) {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    companion object {
        private const val TAG = "RemoteConfigManager"

        // Basic Configuration
        const val WELCOME_MESSAGE_KEY = "welcome_message"
        const val FEATURE_FLAG_KEY = "feature_flag_enabled"
        const val API_URL_KEY = "api_base_url"
        const val APP_VERSION_KEY = "app_version"
        const val MAX_RETRY_ATTEMPTS_KEY = "max_retry_attempts"
        const val CACHE_DURATION_HOURS_KEY = "cache_duration_hours"
        const val ENABLE_DEBUG_LOGGING_KEY = "enable_debug_logging"
        
        // Dynamic Theme & UI
        const val APP_THEME_KEY = "app_theme"
        const val PRIMARY_COLOR_KEY = "primary_color"
        const val SHOW_PREMIUM_FEATURES_KEY = "show_premium_features"
        const val ENABLE_ANIMATIONS_KEY = "enable_animations"
        
        // A/B Testing
        const val BUTTON_STYLE_KEY = "button_style"
        const val ONBOARDING_VARIANT_KEY = "onboarding_variant"
        const val CHECKOUT_FLOW_KEY = "checkout_flow"
        
        // Emergency & Notifications
        const val EMERGENCY_MESSAGE_KEY = "emergency_message"
        const val MAINTENANCE_MODE_KEY = "maintenance_mode"
        const val SHOW_ANNOUNCEMENT_KEY = "show_announcement"
        const val ANNOUNCEMENT_TEXT_KEY = "announcement_text"
        
        // Dynamic Content
        const val FEATURED_PRODUCT_KEY = "featured_product"
        const val DISCOUNT_PERCENTAGE_KEY = "discount_percentage"
        const val MAX_ITEMS_PER_PAGE_KEY = "max_items_per_page"
        
        // Performance & Limits
        const val API_TIMEOUT_SECONDS_KEY = "api_timeout_seconds"
        const val MAX_FILE_UPLOAD_MB_KEY = "max_file_upload_mb"
        const val ENABLE_ANALYTICS_KEY = "enable_analytics"
    }

    init {
        // Configure Remote Config settings
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // 0 for development/testing
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values from XML
        setDefaultValues()
        
        // Log current values for debugging
        logCurrentValues()
    }

    private fun setDefaultValues() {
        // Load default values from XML file
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        Log.d(TAG, "Default values loaded from XML")
    }

    private fun logCurrentValues() {
        Log.d(TAG, "=== Current Remote Config Values ===")
        Log.d(TAG, "Welcome Message: ${remoteConfig.getString(WELCOME_MESSAGE_KEY)}")
        Log.d(TAG, "Feature Flag: ${remoteConfig.getBoolean(FEATURE_FLAG_KEY)}")
        Log.d(TAG, "API URL: ${remoteConfig.getString(API_URL_KEY)}")
        Log.d(TAG, "App Version: ${remoteConfig.getString(APP_VERSION_KEY)}")
        Log.d(TAG, "Max Retry Attempts: ${remoteConfig.getLong(MAX_RETRY_ATTEMPTS_KEY)}")
        Log.d(TAG, "Cache Duration: ${remoteConfig.getLong(CACHE_DURATION_HOURS_KEY)}")
        Log.d(TAG, "Debug Logging: ${remoteConfig.getBoolean(ENABLE_DEBUG_LOGGING_KEY)}")
        Log.d(TAG, "Last fetch time: ${remoteConfig.info.fetchTimeMillis}")
        Log.d(TAG, "Fetch status: ${remoteConfig.info.lastFetchStatus}")
        Log.d(TAG, "=====================================")
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            Log.d(TAG, "Starting Remote Config fetch...")
            val fetchResult = remoteConfig.fetchAndActivate().await()
            Log.d(TAG, "Remote Config fetch completed. Result: $fetchResult")
            
            if (fetchResult) {
                Log.d(TAG, "New values activated!")
                logCurrentValues()
            } else {
                Log.d(TAG, "Values were already up to date")
            }
            
            fetchResult
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching Remote Config", e)
            false
        }
    }

    open fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    open fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    open fun getLong(key: String): Long {
        return remoteConfig.getLong(key)
    }

    open fun getDouble(key: String): Double {
        return remoteConfig.getDouble(key)
    }
}
