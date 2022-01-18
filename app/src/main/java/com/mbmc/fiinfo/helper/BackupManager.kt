package com.mbmc.fiinfo.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CREATE_DOCUMENT
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.mbmc.fiinfo.util.DATABASE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class BackupManager @Inject constructor(@ApplicationContext private val context: Context) {

    class Save : ActivityResultContract<Unit, Uri?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(ACTION_CREATE_DOCUMENT).apply {
                type = MIME
                putExtra(Intent.EXTRA_TITLE, FILE)
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }

    class Restore : ActivityResultContract<Unit, Uri?>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(ACTION_OPEN_DOCUMENT).apply {
                type = MIME
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(MIME))
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
            intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }

    fun save(uri: Uri) {
        val mainFile = context.getDatabasePath(DATABASE_NAME).absolutePath
        val files = mutableListOf<String>()
        EXTENSIONS.forEach {
            files.add(mainFile.plus(it))
        }

        ZipOutputStream(BufferedOutputStream(context.contentResolver.openOutputStream(uri))).use { output ->
            files.forEach { file ->
                BufferedInputStream(File(file).inputStream()).use {
                    output.putNextEntry(ZipEntry(file))
                    it.copyTo(output)
                }
            }
        }
    }

    fun restore(uri: Uri) {
        val files = mutableListOf<String>()
        ZipInputStream(BufferedInputStream(context.contentResolver.openInputStream(uri))).use { input ->
            var zipEntry = input.nextEntry
            while (zipEntry != null) {
                files.add(zipEntry.name)
                BufferedOutputStream(File(zipEntry.name).outputStream()).use {
                    input.copyTo(it)
                }
                zipEntry = input.nextEntry
            }
        }
        // TODO: check extensions too
        if (files.size != FILES) {
            throw Exception()
        }
    }

    companion object {
        private const val FILE = "signal_info.zip"
        private const val MIME = "*/*"
        private val EXTENSIONS = listOf("", "-wal", "-shm")
        private val FILES = EXTENSIONS.size
    }
}