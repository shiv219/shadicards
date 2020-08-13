package com.shadi.repository

import androidx.paging.PagingData
import com.shadi.data.response.Result
import kotlinx.coroutines.flow.Flow

/**
 *  repository class for MatchMaking
 */
interface MatchMakingRepository {
    fun getMatchList(): Flow<PagingData<Result>>
    suspend fun updateMatch(result: Result)
}