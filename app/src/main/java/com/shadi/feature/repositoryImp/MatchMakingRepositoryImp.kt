package com.shadi.feature.repositoryImp

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shadi.data.MatchRemoteMediator
import com.shadi.data.db.CacheDatabase
import com.shadi.data.network.ApiService
import com.shadi.data.response.Result
import com.shadi.repository.MatchMakingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchMakingRepositoryImp @Inject constructor(private val service: ApiService, private val database: CacheDatabase) : MatchMakingRepository {
    @ExperimentalPagingApi
    override fun getMatchList(): Flow<PagingData<Result>> {
//        Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
//            pagingSourceFactory = {
//                MatchPagingSource(service)
//            }).flow

        val pagingSourceFactory = { database.matchDao().fetchMatch() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MatchRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun updateMatch(result: Result) {
        database.matchDao().updateMatch(result)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}