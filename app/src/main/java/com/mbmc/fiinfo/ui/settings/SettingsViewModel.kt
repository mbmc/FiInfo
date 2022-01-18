package com.mbmc.fiinfo.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.database.EventDatabase
import com.mbmc.fiinfo.helper.BackupManager
import com.mbmc.fiinfo.util.Result
import com.mbmc.fiinfo.util.executeAndUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupManager: BackupManager,
    private val eventDatabase: EventDatabase
) : ViewModel() {

    val result = MutableStateFlow<Result>(Result.IsLoading(false))

    suspend fun checkpoint() {
        eventDatabase.checkpoint()
    }

    fun save(uri: Uri) {
        viewModelScope.launch {
            executeAndUpdate(result, R.string.database_save_success, R.string.database_save_error) {
                backupManager.save(uri)
            }
        }
    }

    fun restore(uri: Uri) {
        viewModelScope.launch {
            executeAndUpdate(result, R.string.database_restore_success, R.string.database_restore_error) {
                backupManager.restore(uri)
            }
        }
    }

    fun deleteEvents() {
        viewModelScope.launch {
            executeAndUpdate(result, R.string.database_delete_success, R.string.database_delete_error) {
                eventDatabase.clearAllTables()
            }
        }
    }
}