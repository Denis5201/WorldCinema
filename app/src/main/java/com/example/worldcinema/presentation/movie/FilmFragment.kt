package com.example.worldcinema.presentation.movie

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentFilmBinding
import com.example.worldcinema.databinding.ItemTagBinding
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmFragment : Fragment() {

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backFilm.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
        }
        Glide.with(this.requireContext())
            .load(viewModel.movie.poster)
            .error(R.drawable.logo)
            .into(binding.posterFilm)

        binding.watchEpisode.setOnClickListener {
            if (viewModel.episodes.value != null) {
                viewModel.toEpisodeScreen(viewModel.episodes.value!!.first().episodeId)
            }
        }

        binding.age.text = viewModel.movie.age
        binding.age.setTextColor(Color.parseColor(viewModel.ageColor))

        viewModel.movie.tags.forEach { tag ->
            val newTagBinding = ItemTagBinding.inflate(layoutInflater)
            newTagBinding.tagText.text = tag.name
            binding.tagsLayout.addView(newTagBinding.root)
        }
        binding.descriptionFilm.text = viewModel.movie.description

        if (viewModel.movie.imageUrls.isNotEmpty()) {
            val imagesAdapter = FilmImagesAdapter()
            imagesAdapter.imagesList = viewModel.movie.imageUrls
            binding.imagesFilmRV.adapter = imagesAdapter
        } else {
            binding.imagesTextFilm.visibility = View.GONE
            binding.imagesFilmRV.visibility = View.GONE
        }

        viewModel.episodes.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val episodeAdapter = FilmEpisodeAdapter { episodeId ->
                    viewModel.toEpisodeScreen(episodeId)
                }
                episodeAdapter.episodeList = it
                binding.episodesRV.adapter = episodeAdapter
                binding.episodeTextFilm.visibility = View.VISIBLE
                binding.episodesRV.visibility = View.VISIBLE
            } else {
                binding.episodeTextFilm.visibility = View.GONE
                binding.episodesRV.visibility = View.GONE
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            } else if (it.goToEpisodeScreen) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = FilmFragmentDirections.actionFilmFragmentToEpisodeFragment(
                    it.episodeId, viewModel.movie.movieId, it.episodeString, viewModel.movieString, it.releaseYear
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            }
            if (!it.isLoadingEpisodes) {
                binding.progressBarFilm.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}