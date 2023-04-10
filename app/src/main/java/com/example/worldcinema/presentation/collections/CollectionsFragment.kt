package com.example.worldcinema.presentation.collections

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentCollectionsBinding
import com.example.worldcinema.presentation.base.BaseFragmentDirections
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment : Fragment() {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CollectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        viewModel.collectionList.observe(viewLifecycleOwner) {
            val adapter = CollectionsAdapter {info -> viewModel.goToFilmsCollection(info) }
            adapter.collectionList = it
            binding.collectionRV.adapter = adapter
        }

        binding.addCollection.setOnClickListener {
            viewModel.goToCreateCollection()
        }

        binding.refreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK)
        binding.refreshLayout.setColorSchemeResources(R.color.orange)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getCollectionList()
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goToFilmsCollection) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = BaseFragmentDirections.actionBaseFragmentToFilmsCollectionFragment(it.collectionString)
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.goToCreateCollection) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = BaseFragmentDirections.actionBaseFragmentToEditCollectionFragment(
                    false, null
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
            if (!it.isLoading) {
                binding.progressBarCollections.visibility = View.GONE
            }
            if (!it.isRefreshing) {
                binding.refreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}