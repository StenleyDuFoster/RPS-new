package com.stenleone.rockpaperscissors.managers

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.stenleone.rockpaperscissors.utils.constants.RemoteConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        val config = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(TimeUnit.HOURS.toSeconds(1))
            .build()
        setConfigSettingsAsync(config)
        setDefaultsAsync(RemoteConfig.values().associate { it.key to it.defaultValue })
        fetchAndActivate()
    }

    private val firebaseConfig
        get() = firebaseRemoteConfig.apply { fetchAndActivate() }

    fun getInt(config: RemoteConfig) = firebaseConfig.getLong(config.key).toInt()

    fun getIntAsync(config: RemoteConfig, success: (Int) -> Unit, failure: (String) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                firebaseRemoteConfig.getLong(config.key).toInt().let { intValue -> success(intValue) }
            }
            .addOnFailureListener {
                failure(it.message.toString())
            }
    }

    fun getString(config: RemoteConfig, vararg replace: String): String = firebaseConfig.getString(config.key).let { string ->
        var result = string
        replace.forEach { result = result.replaceFirst("%s", it) }
        return result
    }

    fun getStringAsync(config: RemoteConfig, success: (String) -> Unit, failure: (String) -> Unit, vararg replace: String) {

        firebaseRemoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                firebaseRemoteConfig.getString(config.key).let { string ->
                    var result = string
                    replace.forEach { result = result.replaceFirst("%s", it) }
                    success(result)
                }
            }
            .addOnFailureListener {
                failure(it.message.toString())
            }
    }

    fun getBoolean(config: RemoteConfig) = firebaseConfig.getBoolean(config.key)

}