package com.example.worldcinema.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import com.example.worldcinema.R
import com.example.worldcinema.databinding.FragmentMovieChatBinding
import com.example.worldcinema.domain.model.ChatMessage
import com.example.worldcinema.presentation.createErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieChatFragment : Fragment() {

    private var _binding: FragmentMovieChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieChatAdapter()
        binding.chatFieldRV.adapter = adapter

        binding.nameChat.text = viewModel.chatName

        viewModel.statusConnection.observe(viewLifecycleOwner) { status ->
            if (status == null || status == false) {
                return@observe
            }
            viewModel.message!!.observe(viewLifecycleOwner) {
                if (it != null) {
                    addMessage(it, adapter)
                }
            }
        }

        binding.backMovieChat.setOnClickListener {
            val mainNavHost = requireActivity().supportFragmentManager.findFragmentById(R.id.bigFragment) as NavHost
            mainNavHost.navController.navigateUp()
            viewModel.setDefaultStatus()
        }

        binding.sendMessage.setOnClickListener {
            viewModel.sendMessage(binding.inputMessage.text.toString().trim())
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            if (it.isShowMessage) {
                createErrorDialog(this.requireContext(), it.message) {
                    viewModel.setDefaultStatus()
                }.show()
            }
            if (!it.isLoading) {
                binding.progressBarMovieChat.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        viewModel.disconnect()
        super.onDestroyView()
        _binding = null
    }

    private fun addMessage(chatMessage: ChatMessage, adapter: MovieChatAdapter) {
        if (adapter.componentList.isEmpty()) {
            adapter.addComponent(ChatComponent.DateLabel(chatMessage.creationDateTime.toLocalDate()))
        } else {
            when(val lastMessage = adapter.componentList.last()) {
                is ChatComponent.UsersMessage -> maybeAddDateLabel(lastMessage.chatMessage, chatMessage, adapter)
                is ChatComponent.MyMessage -> maybeAddDateLabel(lastMessage.chatMessage, chatMessage, adapter)
                else -> {}
            }
        }
        if (viewModel.userId == chatMessage.authorId) {
            adapter.addComponent(ChatComponent.MyMessage(chatMessage))
        }
        else {
            adapter.addComponent(ChatComponent.UsersMessage(chatMessage))
        }
    }

    private fun maybeAddDateLabel(last: ChatMessage, new: ChatMessage, adapter: MovieChatAdapter) {
        if (last.creationDateTime.toLocalDate().isEqual(new.creationDateTime.toLocalDate())) {
            return
        }
        adapter.addComponent(ChatComponent.DateLabel(new.creationDateTime.toLocalDate()))
    }
}