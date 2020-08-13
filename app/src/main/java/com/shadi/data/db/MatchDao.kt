package com.shadi.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.shadi.data.response.Result

@Dao
interface  MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: List<Result>)

    @Update
    suspend fun updateMatch(result: Result)

    @Query("select * from match_table")
    fun fetchMatch(): PagingSource<Int, Result>

    @Query("DELETE FROM match_table")
    suspend fun clearRepos()
}