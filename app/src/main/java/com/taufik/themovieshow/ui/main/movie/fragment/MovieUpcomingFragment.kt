package com.taufik.themovieshow.ui.main.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.viewmodel.movie.MovieViewModel
import com.taufik.themovieshow.databinding.FragmentMovieUpcomingBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter

class MovieUpcomingFragment : Fragment() {

    private var _binding: FragmentMovieUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter() = with(binding) {
        rvMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {
        showLoading(true)
        viewModel.apply {
            setMovieUpcoming()
            listUpcoming.observe(viewLifecycleOwner) {
                if (it != null) {
                    movieAdapter.submitList(it)
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressBar.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}