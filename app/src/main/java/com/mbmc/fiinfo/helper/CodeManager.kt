package com.mbmc.fiinfo.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.mbmc.fiinfo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CodeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceManager: PreferenceManager
) {

    fun execute(code: String): Boolean {
        if (preferenceManager.hasSeenCodeInstructions()) {
            openDialer(context, code)
            return true
        }
        return false
    }

    fun setHasSeenInstructions() {
        preferenceManager.setHasSeenCondeInstructions()
    }

    fun openDialer(context: Context, code: String) {
        copy(context, code)
        val intent = Intent(Intent.ACTION_DIAL).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun copy(context: Context, code: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(context.getString(R.string.code), context.getString(R.string.code_dialer, code))
        clipboardManager.setPrimaryClip(clipData)
    }
}