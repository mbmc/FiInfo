package com.mbmc.fiinfo.ui.icons

import androidx.lifecycle.ViewModel
import com.mbmc.fiinfo.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class IconsViewModel : ViewModel() {

    val icons: Flow<List<Icon>> by lazy {
        val icons = mutableListOf<Icon>()
        Type.values().forEach { type ->
            when (type) {
                Type.UNKNOWN -> {
                    // no-op
                }
                Type.CARRIER -> {
                    // Skip Three AT since Three UK will display both
                    Carrier.values().filter { it != Carrier.THREE_AT }.forEach {
                        icons.add(it)
                    }
                }
                Type.WIFI_CARRIER -> {
                    WifiCarrier.values().forEach {
                        icons.add(it)
                    }
                }
                else -> icons.add(type)
            }
        }
        flow {
            emit(icons)
        }
            .flowOn(Dispatchers.IO)
    }
}