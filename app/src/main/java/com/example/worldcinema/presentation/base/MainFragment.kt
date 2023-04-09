package com.example.worldcinema.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentMainBinding
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playLastEpisode.setOnClickListener {
            viewModel.goWatchEpisode()
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goWatchEpisode) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = BaseFragmentDirections.actionBaseFragmentToEpisodeFragment(
                    it.episodeId, it.movieId, null, null, null
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.goToFilmScreen) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = BaseFragmentDirections.actionBaseFragmentToFilmFragment(it.movieString)
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
        }

        viewModel.coverImage.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            Glide.with(this.requireContext())
                .load(it)
                .into(binding.cover)
            binding.cover.visibility = View.VISIBLE
            binding.gradient.visibility = View.VISIBLE
            binding.coverButton.visibility = View.VISIBLE
        }
        viewModel.trendList.observe(viewLifecycleOwner) {
            setData(binding.inTrendText, binding.inTrendRV, it)
        }
        viewModel.lastViewEpisode.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            Glide.with(this.requireContext())
                .load(it.preview)
                .error(R.drawable.logo)
                .into(binding.lastViewCover)

            binding.lastViewText.visibility = View.VISIBLE
            binding.lastViewCover.visibility = View.VISIBLE
            binding.playLastEpisode.visibility = View.VISIBLE

        }
        viewModel.newList.observe(viewLifecycleOwner) {
            setData(binding.newText, binding.newRV, it, true)
        }
        viewModel.forMeList.observe(viewLifecycleOwner) {
            setData(binding.forMeText, binding.forMeRV, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(text: View, recyclerView: RecyclerView, data: List<Movie>, isNew: Boolean = false) {
        if (data.isEmpty()) {
            return
        }
        val adapter = MainAdapter(isNew) { movie -> viewModel.toFilmScreen(movie) }
        adapter.posterList = data
        recyclerView.adapter = adapter
        text.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
    }
}