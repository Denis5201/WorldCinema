package com.example.worldcinema.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentUserChatListBinding
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserChatListFragment : Fragment() {

    private var _binding: FragmentUserChatListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserChatListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserChatList()

        binding.backUserChats.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
            viewModel.setDefaultStatus()
        }

        viewModel.chatList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            val adapter = UserChatListAdapter { chatInfo -> viewModel.goToChat(chatInfo) }
            adapter.chatList = it
            binding.chatListRV.adapter = adapter
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.goToMovieChat) {
                val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost

                val directions = UserChatListFragmentDirections.actionUserChatListFragmentToMovieChatFragment(
                    it.chatId, it.chatName
                )
                mainNavHost.navController.navigate(directions)

                viewModel.setDefaultStatus()
            } else if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
            if (!it.isLoading) {
                binding.progressBarUserChats.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}