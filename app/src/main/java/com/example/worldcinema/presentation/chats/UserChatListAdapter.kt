package com.example.worldcinema.presentation.chats

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemChatBinding
import com.example.worldcinema.domain.model.ChatInfo

class UserChatListAdapter(
    private val click: (ChatInfo) -> Unit
) : RecyclerView.Adapter<UserChatListAdapter.ChatViewHolder>() {

    var chatList: List<ChatInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position], click)
    }

    class ChatViewHolder(private val binding: ItemChatBinding) : ViewHolder(binding.root) {

        fun bind(chatInfo: ChatInfo, click: (ChatInfo) -> Unit) {
            binding.chatIcon.text = getIconText(chatInfo.chatName)

            binding.nameChatItem.text = chatInfo.chatName

            binding.lastMessage.text = chatInfo.lastMessage?.let {
                getLastMessageText(it.authorName, it.text, this.itemView.context)
            } ?: ""

            binding.root.setOnClickListener {
                click(chatInfo)
            }
        }

        private fun getIconText(chatName: String): String {
            return chatName.split(' ', ignoreCase = false, limit = 2)
                .joinToString {
                    it[0].uppercase()
                }
        }

        private fun getLastMessageText(authorName: String, textMessage: String, context: Context): SpannableString {
            val viewText = SpannableString("$authorName: $textMessage")
            viewText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(context, R.color.grey)
                ),
                0,
                authorName.length + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return viewText
        }
    }
}