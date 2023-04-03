package com.example.worldcinema.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentEpisodeBinding
import com.example.worldcinema.presentation.createErrorDialog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeFragment : Fragment() {

    private var _binding: FragmentEpisodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = ExoPlayer.Builder(this.requireContext()).build()
        binding.videoView.player = player

        binding.backEpisode.setOnClickListener {
            viewModel.saveVideoPosition(player.currentPosition, EpisodeViewModel.BACK)
            player.stop()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.saveVideoPosition(player.currentPosition, EpisodeViewModel.BACK)
            player.stop()
        }

        binding.videoView.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        viewModel.episode.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            binding.nameEpisode.text = it.name
            binding.movieYearEpisode.text = it.year.toString()
            binding.descriptionEpisode.text = it.description

            val video = MediaItem.fromUri(it.filePath)
            player.setMediaItem(video)
            player.prepare()
        }
        viewModel.movie.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.moviePosterEpisode.visibility = View.GONE
                binding.movieNameEpisode.visibility = View.GONE
                binding.movieYearEpisode.visibility = View.GONE
                binding.movieChatEpisode.visibility = View.GONE
                binding.heartEpisode.visibility = View.GONE
                binding.plusEpisode.visibility = View.GONE
                return@observe
            }
            binding.moviePosterEpisode.visibility = View.VISIBLE
            binding.movieNameEpisode.visibility = View.VISIBLE
            binding.movieYearEpisode.visibility = View.VISIBLE
            if (it.chatInfo != null) {
                binding.movieChatEpisode.visibility = View.VISIBLE
            }
            binding.heartEpisode.visibility = View.VISIBLE
            binding.plusEpisode.visibility = View.VISIBLE

            Glide.with(this.requireContext())
                .load(it.poster)
                .into(binding.moviePosterEpisode)
            binding.movieNameEpisode.text = it.name
        }

        viewModel.timePosition.observe(viewLifecycleOwner) {
            if (it + 5 < viewModel.episode.value!!.runtime) {
                player.seekTo(it*1000)
            }
        }

        binding.movieChatEpisode.setOnClickListener {
            viewModel.saveVideoPosition(player.currentPosition, EpisodeViewModel.CHAT)
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.mayNavigateBack) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
                mainNavHost.navController.navigateUp()
                viewModel.setDefaultStatus()
            } else if (it.goToChatScreen) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
                mainNavHost.navController.navigateUp() //!!!!!!!!!!!!TO CHAT
                viewModel.setDefaultStatus()
            } else  if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}