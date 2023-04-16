package com.example.worldcinema.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.worldcinema.databinding.FragmentMovieChatBinding
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}