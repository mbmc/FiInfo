package com.mbmc.fiinfo.ui.codes

import androidx.lifecycle.ViewModel
import com.mbmc.fiinfo.data.Code
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CodesViewModel(private val all: Boolean) : ViewModel() {

    var codes: Flow<List<Code>> =
        if (all) {
            getAll()
        } else {
            getMain()
        }.flowOn(Dispatchers.IO)

    private fun getAll(): Flow<List<Code>> = flow {
        emit(Code.values().toList())
    }

    private fun getMain(): Flow<List<Code>> = flow {
        emit(Code.MAIN.toList())
    }
}