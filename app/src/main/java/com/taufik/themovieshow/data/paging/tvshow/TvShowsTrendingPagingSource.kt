package com.taufik.themovieshow.data.paging.tvshow

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult
import com.taufik.themovieshow.utils.CommonConstants
import retrofit2.HttpException

class TvShowsTrendingPagingSource(
    private val repository: TheMovieShowRepository
) : PagingSource<Int, TvShowsTrendingResult>() {
    override fun getRefreshKey(state: PagingState<Int, TvShowsTrendingResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowsTrendingResult> {
        val currentPage = params.key ?: CommonConstants.STARTING_PAGE_INDEX
        return try {
            val response = repository.getTvShowsTrending(currentPage)
            val data = response.body()?.results
            val responseData = mutableListOf<TvShowsTrendingResult>()
            if (data != null) responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == CommonConstants.STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (data?.isEmpty() == true) null else currentPage + 1
            )
        } catch (httpEx: HttpException) {
            LoadResult.Error(httpEx)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}