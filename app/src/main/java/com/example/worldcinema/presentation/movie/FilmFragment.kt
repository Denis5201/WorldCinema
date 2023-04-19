package com.example.worldcinema.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            if (!viewModel.episodes.value.isNullOrEmpty()) {
                viewModel.toEpisodeScreen(viewModel.episodes.value!!.first().episodeId)
            }
        }

        binding.age.text = viewModel.movie.age
        binding.age.setTextColor(
            ContextCompat.getColor(requireContext(), getIdColor(viewModel.movie.age))
        )

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

        if (viewModel.movie.chatInfo != null) {
            binding.chatFIlm.visibility = View.VISIBLE
            binding.chatFIlm.setOnClickListener {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = FilmFragmentDirections.actionFilmFragmentToMovieChatFragment(
                    viewModel.movie.chatInfo!!.chatId, viewModel.movie.chatInfo!!.name
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            }
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

    private fun getIdColor(colorString: String): Int {
        return when(colorString) {
            "0+" -> R.color.transparent
            "6+" -> R.color.age6
            "12+" -> R.color.age12
            "16+" -> R.color.age16
            else -> R.color.orange
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}