package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class DiscoverMovieAdapter : ListAdapter<DiscoverMovieResult, DiscoverMovieAdapter.MovieViewHolder>(DiscoverMovieDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverMovieResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(DetailMovieFragment.EXTRA_DATA, data)
                    it.findNavController().navigate(R.id.detailMovieFragment, bundle)
                }
            }
        }
    }

    object DiscoverMovieDiffCallback: DiffUtil.ItemCallback<DiscoverMovieResult>() {
        override fun areItemsTheSame(
            oldItem: DiscoverMovieResult,
            newItem: DiscoverMovieResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DiscoverMovieResult,
            newItem: DiscoverMovieResult
        ): Boolean = oldItem == newItem
    }
}