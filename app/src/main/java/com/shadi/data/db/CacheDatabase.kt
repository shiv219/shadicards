package com.shadi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shadi.data.response.Result

@Database(entities = [Result::class,RemoteKeys::class], version = 2,exportSchema = false)
abstract class CacheDatabase : RoomDatabase(){
    abstract fun matchDao(): MatchDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}