package com.mbmc.fiinfo.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(FILE, MODE_PRIVATE)

    fun hasSeenCodeInstructions(): Boolean =
        sharedPreferences.getBoolean(CODE_INSTRUCTIONS, false)

    fun setHasSeenCondeInstructions() {
        setBoolean(CODE_INSTRUCTIONS, true)
    }

    fun hasSeenWelcome(): Boolean =
        sharedPreferences.getBoolean(WELCOME, false)

    fun setHasSeenWelcome() {
        setBoolean(WELCOME, true)
    }

    private fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
        }.apply()
    }

    companion object {
        private const val FILE = "preferences"
        private const val CODE_INSTRUCTIONS = "code_instructions"
        private const val WELCOME = "welcome_v0.2.0"
    }
}