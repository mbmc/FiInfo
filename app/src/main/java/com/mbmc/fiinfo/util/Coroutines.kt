package com.mbmc.fiinfo.util

import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

sealed class Result(val messageId: Int = 0) {
    data class IsLoading(val state: Boolean) : Result()
    data class Success(@StringRes val stringId: Int) : Result(stringId)
    data class Error(@StringRes val stringId: Int) : Result(stringId)
}

suspend fun executeAndUpdate(
    result: MutableStateFlow<Result>,
    @StringRes successId: Int,
    @StringRes errorId: Int,
    function: () -> (Unit)
) {
    result.value = Result.IsLoading(true)
    try {
        withContext(Dispatchers.IO) {
            function()
        }
        result.value = Result.Success(successId)
    } catch (exception: Exception) {
        result.value = Result.Error(errorId)
    } finally {
        result.value = Result.IsLoading(false)
    }
}