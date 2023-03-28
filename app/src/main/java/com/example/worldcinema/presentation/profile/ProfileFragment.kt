package com.example.worldcinema.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentProfileBinding
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        binding.exit.setOnClickListener {
            viewModel.logout()
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigate(R.id.action_baseFragment_to_signInFragment)
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.isLoading) {
                binding.avatarProfile.visibility = View.INVISIBLE
                binding.nameProfile.visibility = View.INVISIBLE
                binding.emailProfile.visibility = View.INVISIBLE
            } else {
                binding.avatarProfile.visibility = View.VISIBLE
                binding.nameProfile.visibility = View.VISIBLE
                binding.emailProfile.visibility = View.VISIBLE
            }
            if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.closeDialog()
                }.show()
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) {
            binding.nameProfile.text = it.userName
            binding.emailProfile.text = it.email

            Glide.with(this.requireContext())
                .load(it.avatar)
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .fallback(R.drawable.avatar_default)
                .circleCrop()
                .into(binding.avatarProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}