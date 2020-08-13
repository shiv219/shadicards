package com.shadi.di

import android.content.Context
import androidx.room.Room
import com.shadi.data.db.CacheDatabase
import com.shadi.data.db.MatchDao
import com.shadi.data.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun cacheDatabase(@ApplicationContext context: Context): CacheDatabase {
        return Room.databaseBuilder(context, CacheDatabase::class.java, "shadi.db").build()
    }

    @Provides
    fun provideRemoteKeyDao(database: CacheDatabase): RemoteKeysDao = database.remoteKeysDao()

    @Provides
    fun provideMatchDao(database: CacheDatabase): MatchDao = database.matchDao()


}