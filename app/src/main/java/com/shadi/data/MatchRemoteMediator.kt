/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shadi.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shadi.data.db.CacheDatabase
import com.shadi.data.db.RemoteKeys
import com.shadi.data.network.ApiService
import com.shadi.data.response.Result
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class MatchRemoteMediator(
    private val service: ApiService,
    private val cacheDatabase: CacheDatabase
) : RemoteMediator<Int, Result>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: return MediatorResult.Error(InvalidObjectException("Remote key and the prevKey should not be null"))

                // The LoadType is PREPEND so some data was loaded before,
                // so we should have been able to get remote keys
                // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                // If the previous key is null, then we can't request more data
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    return MediatorResult.Error(InvalidObjectException("Remote key and the nextKey should not be null"))
                }
                remoteKeys.nextKey
            }

        }
        try {
            val apiResponse =
                service.getMatches(resultCount = state.config.pageSize, pageCount = page)

            val repos = apiResponse.results
            val endOfPaginationReached = repos?.isEmpty()
            cacheDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    cacheDatabase.remoteKeysDao().clearRemoteKeys()
                    cacheDatabase.matchDao().clearRepos()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached!!) null else page + 1
                val keys = repos?.map {
                    RemoteKeys(repoId = it.login?.uuid!!, prevKey = prevKey, nextKey = nextKey)
                }
                cacheDatabase.remoteKeysDao().insertAll(keys)
                cacheDatabase.matchDao().insert(repos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached!!)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                cacheDatabase.remoteKeysDao().remoteKeysRepoId(repo.login?.uuid!!)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Result>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                cacheDatabase.remoteKeysDao().remoteKeysRepoId(repo.login?.uuid!!)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Result>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.login?.uuid?.let { repoId ->
                cacheDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

}