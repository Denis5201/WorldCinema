package com.example.worldcinema.presentation.base

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentCompilationBinding
import com.example.worldcinema.presentation.createErrorDialog
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompilationFragment : Fragment(), CardStackListener {

    private var _binding: FragmentCompilationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompilationViewModel by viewModels()
    private val adapter by lazy { CompilationAdapter() }
    private lateinit var manager: CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompilationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        viewModel.getFavourFilms()

        binding.currentMovieName.isSelected = true

        manager = CardStackLayoutManager(requireContext(), this)
        binding.cardStack.layoutManager = manager


        binding.crossButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStack.swipe()
        }
        binding.movieButton.setOnClickListener {
            viewModel.toFilmScreen(adapter.movieList[manager.topPosition])
        }
        binding.heartButton.setOnClickListener {
            viewModel.changeFilmFavourStatus(adapter.movieList[manager.topPosition].movieId)
        }

        viewModel.alreadyInFavour.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            if (it) {
                binding.heartButton.setImageResource(R.drawable.collection_icon_19)
                binding.heartButton.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.orange), PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.heartButton.setImageResource(R.drawable.small_heart)
            }
        }
        viewModel.listFavourMovie.observe(viewLifecycleOwner) {
            if (manager.topPosition < adapter.movieList.size && adapter.movieList.isNotEmpty()) {
                viewModel.isFilmInFavour(adapter.movieList[manager.topPosition].movieId)
            }
        }

        viewModel.compilation.observe(viewLifecycleOwner) {
            adapter.movieList = it
                .filterIndexed { index, _ -> index >= viewModel.swipedCounter }
                .toMutableList()
            if (adapter.movieList.isEmpty()) {
                binding.buttonsAndText.visibility = View.INVISIBLE
            } else {
                binding.buttonsAndText.visibility = View.VISIBLE
            }
            binding.cardStack.adapter = adapter
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                binding.progressBarCompilation.visibility = View.GONE
                binding.compilationEmptyImage.visibility = View.VISIBLE
                binding.compilationEmptyText.visibility = View.VISIBLE
            }
            if (it.goToFilmScreen) {
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
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> {
                viewModel.dislike(adapter.movieList[manager.topPosition - 1].movieId)
                viewModel.itemSwiped()
            }
            Direction.Right -> {
                viewModel.itemSwiped()
            }
            else -> {}
        }
        if (manager.topPosition >= adapter.movieList.size) {
            binding.buttonsAndText.visibility = View.INVISIBLE
        }
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {
        binding.currentMovieName.text = adapter.movieList[position].name
        viewModel.isFilmInFavour(adapter.movieList[position].movieId)
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}