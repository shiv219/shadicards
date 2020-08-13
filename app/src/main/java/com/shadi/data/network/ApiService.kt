package com.shadi.data.network

import com.shadi.data.response.MatchMakingResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * interface for match
 */
interface ApiService {
    @GET("api/")
    suspend fun getMatches(
        @Query("results") resultCount: Int = 10,
        @Query("page") pageCount: Int
    ): MatchMakingResponse
}