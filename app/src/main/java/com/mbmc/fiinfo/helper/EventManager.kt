package com.mbmc.fiinfo.helper

import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.data.Type
import com.mbmc.fiinfo.database.EventDao
import com.mbmc.fiinfo.util.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventManager @Inject constructor(
    private val eventDao: EventDao,
    private val broadcastManager: BroadcastManager
) {

    private var lastEvent: Event? = null
    private var mutex = Mutex()

    fun log(event: Event) {
        val copy = event.copy()
        CoroutineScope(Dispatchers.Main).launch {
            syncLog(copy)
        }
    }

    private suspend fun syncLog(event: Event) {
        mutex.withLock {
            if (event.type == Type.DISCONNECTED) {
                broadcastManager.update(event)
                return
            }

            if (lastEvent == null) {
                eventDao.getLast()?.let {
                    lastEvent = it
                }
            }

            if (lastEvent?.isEqualTo(event) == true) {
                return
            }

            lastEvent = event

            withContext(Dispatchers.IO) {
                eventDao.addEvent(event.toEntity())
            }

            broadcastManager.update(event)
        }
    }
}