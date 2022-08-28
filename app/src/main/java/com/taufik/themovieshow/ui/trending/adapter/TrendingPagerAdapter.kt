package com.taufik.themovieshow.ui.trending.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.movie.fragment.MovieTrendingFragment
import com.taufik.themovieshow.ui.tvshow.fragment.TvShowTrendingFragment

class TrendingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieTrendingFragment()
            1 -> fragment = TvShowTrendingFragment()
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}