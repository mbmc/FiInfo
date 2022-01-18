package com.mbmc.fiinfo.ui.stats

import androidx.lifecycle.ViewModel
import com.mbmc.fiinfo.data.Stat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    val stats: Flow<List<Stat>> by lazy {
        statsRepository
            .getStats()
            .flowOn(Dispatchers.IO)
    }
}