package com.taufik.themovieshow.utils

import com.taufik.themovieshow.BuildConfig

object CommonConstants {
    const val STARTING_PAGE_INDEX = 1
    const val LOAD_PER_PAGE = 5
    const val LOAD_MAX_PER_PAGE = 25
    const val TABLE_NAME_FAVORITE_MOVIE_ENTITY = "favorite_movie"
    const val COLUMN_NAME_POSTER = "poster"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_RELEASE_DATE = "release_date"
    const val COLUMN_NAME_FIRST_AIR_DATE = "first_air_date"
    const val COLUMN_NAME_RATING = "rating"
    const val TABLE_NAME_FAVORITE_TV_SHOW_ENTITY = "favorite_tv_show"
    const val DB_NAME = "the_movie_show_db"
    const val DB_VERSION = 1
    const val QUERY_API_KEY = "api_key"
    const val QUERY_Q = "query"
    const val QUERY_MOVIE_ID = "movie_id"
    const val QUERY_TV_SHOW_ID = "tv_id"
    const val API_KEY = BuildConfig.API_KEY
}