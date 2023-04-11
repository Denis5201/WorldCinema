package com.example.worldcinema.presentation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavHost
import androidx.navigation.navGraphViewModels
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentEditCollectionBinding
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCollectionFragment : Fragment() {

    private var _binding: FragmentEditCollectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditCollectionViewModel by navGraphViewModels(R.id.editCollectionFragment) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backEditCollection.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
        }

        binding.saveCollection.setOnClickListener {
            viewModel.saveCollection(binding.nameEditCollection.text.toString())
        }

        if (viewModel.isChanging) {
            binding.deleteCollection.visibility = View.VISIBLE
            binding.deleteCollection.setOnClickListener {
                viewModel.deleteCollection()
            }
        }

        viewModel.collection?.name?.let {
            binding.nameEditCollection.setText(it)
        }
        if (viewModel.collectionIcon.value.isNullOrEmpty()) {
            binding.imageEditCollection.setImageResource(R.drawable.collection_icon_1)
        }

        viewModel.collectionIcon.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            val context = requireContext()
            val iconId = context.resources.getIdentifier(it, "drawable", context.packageName)
            binding.imageEditCollection.setImageResource(iconId)
        }

        binding.chossingIconCollection.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigate(R.id.action_editCollectionFragment_to_choosingCollectionIconFragment)
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goBackAfterSave) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
                mainNavHost.navController.popBackStack()

                viewModel.setDefaultStatus()
            } else if (it.isShowMessage) {
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