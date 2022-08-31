package com.taufik.themovieshow.ui.detail.movie.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.trending.MovieTrendingResult
import com.taufik.themovieshow.data.viewmodel.movie.DetailMovieViewModel
import com.taufik.themovieshow.databinding.FragmentDetailMovieBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieCastAdapter
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by viewModels()

    private var idMovie = 0
    private var title = ""
    private var isChecked = false

    private lateinit var castAdapter: MovieCastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        showToolbarData()
        setData()
        setCastAdapter()
        setReadMore()
    }

    private fun getBundleData() {
        val data = arguments?.getParcelable<MovieTrendingResult>(EXTRA_DATA) as MovieTrendingResult
        idMovie = data.id
        title = data.title
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailMovie.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbarDetailMovie.tvToolbar.text = title
    }

    private fun setData() = with(binding) {
        viewModel.apply {
            setDetailMovies(idMovie)
            getDetailMovies().observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.title
                    tvReleaseDate.text = it.releaseDate
                    tvStatus.text = it.status
                    tvOverview.text = it.overview
                    tvRating.text = toRating(it.voteAverage)

                    when {
                        it.genres.isEmpty() -> tvGenre.text = "N/A"
                        else -> tvGenre.text = it.genres[0].name
                    }

                    tvRuntime.text = String.format("${it.runtime} min")

                    checkFavoriteData(idMovie)
                    setActionFavorite(idMovie, it.posterPath, title, it.releaseDate, it.voteAverage)
                    shareMovie(it.homepage)
                    setCast(idMovie)
                    showVideo(idMovie)
                }
            }
        }
    }

    private fun checkFavoriteData(id: Int) = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkFavorite(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        toolbarDetailMovie.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        toolbarDetailMovie.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }
    }

    private fun setActionFavorite(
        id: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        voteAverage: Double
    ) = with(binding) {
        toolbarDetailMovie.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    id,
                    posterPath,
                    title,
                    releaseDate,
                    voteAverage
                )
                showToasty("Added to favorite")
            } else {
                viewModel.removeFromFavorite(id)
                showToasty("Removed from favorite")
            }
        }
    }

    private fun shareMovie(link: String) = with(binding) {
        toolbarDetailMovie.imgShare.setOnClickListener {
            try {
                val body = "Visit this awesome movie \n${link}"
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                startActivity(Intent.createChooser(shareIntent, "Share with:"))
            } catch (e: Exception) {
                Log.e("shareFailed", "onOptionsItemSelected: ${e.localizedMessage}")
            }
        }
    }

    private fun showVideo(id: Int) = with(binding) {
        viewModel.apply {
            setDetailMovieVideo(id)
            getDetailMovieVideo().observe(viewLifecycleOwner) {
                if (it != null) {
                    when {
                        it.results.isEmpty() -> tvTrailer.text = String.format(Locale.getDefault(), "Trailer Video Not Available")
                        else -> tvTrailer.text = it.results[0].name
                    }

                    lifecycle.addObserver(videoTrailer)
                    videoTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            when {
                                it.results.isEmpty() -> Log.e("videoFailed", "onReady: ")
                                else -> {
                                    val videoId = it.results[0].key
                                    youTubePlayer.loadVideo(videoId, 0F)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    private fun setCastAdapter() = with(binding) {
        castAdapter = MovieCastAdapter()
        rvMovieCast.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailMovieCast(id)
            getDetailMovieCast().observe(viewLifecycleOwner) {
                if (it != null) {
                    castAdapter.submitList(it)
                }
            }
        }
    }

    private fun setReadMore() = with(binding) {
        tvReadMore.isVisible = true
        tvReadMore.setOnClickListener {
            if (tvReadMore.text.toString() == "Read More") {
                tvOverview.maxLines = Integer.MAX_VALUE
                tvOverview.ellipsize = null
                tvReadMore.text = getString(R.string.tvReadLess)
            } else {
                tvOverview.maxLines = 4
                tvOverview.ellipsize = TextUtils.TruncateAt.END
                tvReadMore.text = getString(R.string.tvReadMore)
            }
        }
    }

    private fun showToasty(message: String) {
        Toasty.success(requireContext(), message, Toast.LENGTH_SHORT, true).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "com.taufik.themovieshow.ui.detail.movie.fragment.EXTRA_DATA"
    }
}