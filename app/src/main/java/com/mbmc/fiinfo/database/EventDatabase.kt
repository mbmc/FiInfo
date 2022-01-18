package com.mbmc.fiinfo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.mbmc.fiinfo.util.DATABASE_NAME

@Database(entities = [EventEntity::class], version = 4)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    suspend fun checkpoint() {
        eventDao().checkpoint(SimpleSQLiteQuery(CHECKPOINT))
    }

    companion object {
        private const val CHECKPOINT = "pragma wal_checkpoint(full)"

        @Volatile
        private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EventDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java, DATABASE_NAME
            )
                .addMigrations(MIGRATION_3_4)
                .build()
    }
}