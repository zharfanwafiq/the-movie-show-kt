package com.taufik.themovieshow.data.paging.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import retrofit2.HttpException

class MovieTrendingPagingSource(
    private val repository: TheMovieShowRepository
): PagingSource<Int, MovieTrendingResult>() {
    override fun getRefreshKey(state: PagingState<Int, MovieTrendingResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieTrendingResult> {
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = repository.getMovieTrendingDay(currentPage)
            val data = response.body()?.results
            val responseData = mutableListOf<MovieTrendingResult>()
            if (data != null) responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (httpEx: HttpException) {
            LoadResult.Error(httpEx)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}