package com.shadi.paging.pagingSource

import androidx.paging.PagingSource
import com.shadi.data.network.ApiService
import com.shadi.data.response.Result
import retrofit2.HttpException

/**
 * PagingSource class to handle paging calls
 * use this class without Room i.e network only
 */
private const val STARTING_PAGE_INDEX = 1

class MatchPagingSource(
    private val service: ApiService
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getMatches(
                pageCount = position
            )
            LoadResult.Page(
                data = response.results!!,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.results.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}