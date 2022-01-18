package com.mbmc.fiinfo.di

import android.content.Context
import com.mbmc.fiinfo.database.EventDao
import com.mbmc.fiinfo.database.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MainModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EventDatabase =
        EventDatabase.getInstance(context)

    @Provides
    fun provideDao(eventDatabase: EventDatabase): EventDao =
        eventDatabase.eventDao()
}