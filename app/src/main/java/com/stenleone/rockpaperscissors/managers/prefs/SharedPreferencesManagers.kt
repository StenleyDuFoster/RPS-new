package com.stenleone.rockpaperscissors.managers.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManagers @Inject constructor(@ApplicationContext private val context: Context) {

}