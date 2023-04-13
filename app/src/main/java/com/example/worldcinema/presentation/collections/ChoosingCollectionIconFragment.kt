package com.example.worldcinema.presentation.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavHost
import com.example.worldcinema.Constants
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentChoosingCollectionIconBinding
import com.example.worldcinema.databinding.ItemIconBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosingCollectionIconFragment : Fragment() {

    private var _binding: FragmentChoosingCollectionIconBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosingCollectionIconBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backChoosingIcon.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
        }

        val context = requireContext()

        for (i in 1..36) {
            val newIconBinding =  ItemIconBinding.inflate(layoutInflater)
            val iconId = context.resources.getIdentifier("$ICON_PREFIX$i", "drawable", context.packageName)
            newIconBinding.itemIcon.setImageResource(iconId)
            newIconBinding.itemIcon.setOnClickListener {
                setFragmentResult(
                    Constants.ICON_COLLECTION_REQUEST,
                    bundleOf(Constants.ICON_PARAMETER to "$ICON_PREFIX$i")
                )
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
                mainNavHost.navController.popBackStack()
            }
            newIconBinding.root.id = View.generateViewId()
            binding.layout.addView(newIconBinding.root)
            binding.iconFlow.addView(newIconBinding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ICON_PREFIX = "collection_icon_"
    }
}