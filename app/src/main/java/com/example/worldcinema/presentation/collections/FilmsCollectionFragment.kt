package com.example.worldcinema.presentation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.worldcinema.Constants
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentFilmsCollectionBinding
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsCollectionFragment : Fragment() {

    private var _binding: FragmentFilmsCollectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmsCollectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmsCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backCollectionFilm.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
        }

        val adapter = FilmsCollectionAdapter { movie -> viewModel.toFilm(movie) }

        if (viewModel.collectionInfo.name == Constants.FAVOUR_COLLECTION) {
            binding.editCollection.visibility = View.GONE
        } else {
            binding.editCollection.setOnClickListener {
                viewModel.toEditCollection()
            }
        }

        binding.nameCollectionFilms.text = viewModel.collectionInfo.name

        viewModel.filmsCollection.observe(viewLifecycleOwner) {
            adapter.movieList = it.toMutableList()
            binding.filmsCollectionRV.adapter = adapter

            val helperCallback = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean { return false }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.absoluteAdapterPosition
                    viewModel.deleteFilm(adapter.movieList[position].movieId)
                    adapter.deleteFilm(position)
                }
            }
            val itemTouchHelper = ItemTouchHelper(helperCallback)
            itemTouchHelper.attachToRecyclerView(binding.filmsCollectionRV)
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goToEditCollection) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = FilmsCollectionFragmentDirections.actionFilmsCollectionFragmentToEditCollectionFragment(
                    true, viewModel.collectionString
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.goToFilm) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = FilmsCollectionFragmentDirections.actionFilmsCollectionFragmentToFilmFragment(
                    it.movieString
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
            if (!it.isLoading) {
                binding.progressBarFilmsCollection.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}