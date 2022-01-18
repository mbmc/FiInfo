package com.mbmc.fiinfo.database

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.mbmc.fiinfo.data.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY _id DESC")
    @RewriteQueriesToDropUnusedColumns
    fun getAll(): PagingSource<Int, Event>

    @Query("SELECT * FROM event ORDER BY _id DESC LIMIT 1")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getLast(): Event?

    @Query("SELECT count(*) AS count, type, ssid, mccmnc, speed FROM event GROUP BY type, ssid, mccmnc, speed ORDER BY count DESC")
    fun getStats(): Flow<List<Stat>>

    @Insert
    suspend fun addEvent(eventEntity: EventEntity)

    @RawQuery
    suspend fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery): Int
}