package com.mbmc.fiinfo.ui.stats

import com.mbmc.fiinfo.data.Stat
import com.mbmc.fiinfo.database.EventDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatsRepository @Inject constructor(private val eventDao: EventDao) {

    fun getStats(): Flow<List<Stat>> =
        eventDao.getStats()
}