package com.mbmc.fiinfo.ui.dashboard

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.database.EventDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun getAll(): Flow<PagingData<Event>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH,
                enablePlaceholders = true,
                maxSize = MAX_SIZE
            ),
            pagingSourceFactory = { eventDao.getAll() }
        ).flow

    companion object {
        private const val PAGE_SIZE = 500
        private const val PREFETCH = 50
        private const val MAX_SIZE = 1000
    }
}