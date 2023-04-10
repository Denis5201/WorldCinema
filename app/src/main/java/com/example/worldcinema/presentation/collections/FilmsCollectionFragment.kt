package com.example.worldcinema.presentation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
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

        if (viewModel.collectionInfo.name == Constants.FAVOUR_COLLECTION) {
            binding.editCollection.visibility = View.GONE
        }

        binding.nameCollectionFilms.text = viewModel.collectionInfo.name

        viewModel.filmsCollection.observe(viewLifecycleOwner) {
            val adapter = FilmsCollectionAdapter()
            adapter.movieList = it
            binding.filmsCollectionRV.adapter = adapter
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goToEditCollection) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = FilmsCollectionFragmentDirections.actionFilmsCollectionFragmentToEditCollectionFragment(
                    true, it.collectionString
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