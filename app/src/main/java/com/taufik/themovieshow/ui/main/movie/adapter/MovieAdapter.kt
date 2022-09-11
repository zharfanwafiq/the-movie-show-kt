package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.DetailMovieFragment
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class MovieAdapter : ListAdapter<MovieMainResult, MovieAdapter.MovieViewHolder>(MovieDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieMainResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(DetailMovieFragment.EXTRA_ID, data.id)
                    bundle.putString(DetailMovieFragment.EXTRA_TITLE, data.title)
                    it.findNavController().navigate(R.id.detailMovieFragment, bundle)
                }
            }
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieMainResult>(){
        override fun areItemsTheSame(oldItem: MovieMainResult, newItem: MovieMainResult): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieMainResult, newItem: MovieMainResult): Boolean = oldItem == newItem
    }
}