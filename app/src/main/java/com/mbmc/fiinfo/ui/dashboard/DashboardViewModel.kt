package com.mbmc.fiinfo.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.mbmc.fiinfo.data.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val events: Flow<PagingData<Event>> by lazy {
        eventRepository
            .getAll()
            .flowOn(Dispatchers.IO)
    }
}