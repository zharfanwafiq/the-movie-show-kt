package com.taufik.themovieshow.ui.feature.tvshow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteTvShow::class], version = 1)
abstract class TvShowDatabase: RoomDatabase() {
    companion object {

        private var INSTANCE : TvShowDatabase? = null

        fun getDatabase(context: Context): TvShowDatabase? {
            if (INSTANCE == null) {
                synchronized(TvShowDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, TvShowDatabase::class.java, "tv_show_database").build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun favoriteTvShowDao(): FavoriteTvShowDao
}