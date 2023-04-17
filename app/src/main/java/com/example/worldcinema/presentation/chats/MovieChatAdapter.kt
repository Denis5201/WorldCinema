package com.example.worldcinema.presentation.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemDateBinding
import com.example.worldcinema.databinding.ItemLeftMessageBinding
import com.example.worldcinema.databinding.ItemRightMessageBinding
import com.example.worldcinema.domain.model.ChatMessage
import java.time.LocalDate

class MovieChatAdapter : RecyclerView.Adapter<ViewHolder>() {

    val componentList: MutableList<ChatComponent> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return when(componentList[position]) {
            is ChatComponent.MyMessage -> MY_MESSAGE
            is ChatComponent.UsersMessage -> USERS_MESSAGE
            is ChatComponent.DateLabel -> DATE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            MY_MESSAGE -> MyMessageViewHolder.from(parent, inflater)
            USERS_MESSAGE -> UsersMessageViewHolder.from(parent, inflater)
            DATE -> DateViewHolder.from(parent, inflater)
            else -> throw Exception("Unknown viewType")
        }
    }

    override fun getItemCount(): Int = componentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val component = componentList[position]
        when(holder) {
            is MyMessageViewHolder -> holder.bind(component as ChatComponent.MyMessage)
            is UsersMessageViewHolder -> holder.bind(component as ChatComponent.UsersMessage)
            is DateViewHolder -> holder.bind(component as ChatComponent.DateLabel)
        }
    }

    class MyMessageViewHolder(private val binding: ItemRightMessageBinding) : ViewHolder(binding.root) {

        fun bind(myMessage: ChatComponent.MyMessage) {
            val message = myMessage.chatMessage

            binding.textRight.text = message.text

            binding.nameTimeRight.text = getNameAndTimeString(message)

            val destiny = this.itemView.context.resources.displayMetrics.density
            val params = binding.messageLayoutRight.layoutParams as MarginLayoutParams
            if (myMessage.smallPadding) {
                binding.avatarRight.visibility = View.INVISIBLE

                params.bottomMargin = (SMALL_PADDING * destiny + 0.5f).toInt()
                binding.messageLayoutRight.layoutParams = params
                return
            } else {
                binding.avatarRight.visibility = View.VISIBLE

                params.bottomMargin = (BIG_PADDING * destiny + 0.5f).toInt()
                binding.messageLayoutRight.layoutParams = params
            }

            if (message.authorAvatar.isNullOrEmpty()) {
                binding.avatarRight.setImageResource(R.drawable.avatar_default)
                return
            }
            Glide.with(this.itemView.context)
                .load(message.authorAvatar)
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .circleCrop()
                .into(binding.avatarRight)
        }

        companion object {
            fun from(parent: ViewGroup, inflater: LayoutInflater): MyMessageViewHolder {
                val binding = ItemRightMessageBinding.inflate(inflater, parent, false)
                return MyMessageViewHolder(binding)
            }
        }
    }

    class UsersMessageViewHolder(private val binding: ItemLeftMessageBinding) : ViewHolder(binding.root) {

        fun bind(usersMessage: ChatComponent.UsersMessage) {
            val message = usersMessage.chatMessage

            binding.textLeft.text = message.text

            binding.nameTimeLeft.text = getNameAndTimeString(message)

            val destiny = this.itemView.context.resources.displayMetrics.density
            val params = binding.messageLayoutLeft.layoutParams as MarginLayoutParams
            if (usersMessage.smallPadding) {
                binding.avatarLeft.visibility = View.INVISIBLE

                params.bottomMargin = (SMALL_PADDING * destiny + 0.5f).toInt()
                binding.messageLayoutLeft.layoutParams = params
                return
            } else {
                binding.avatarLeft.visibility = View.VISIBLE

                params.bottomMargin = (BIG_PADDING * destiny + 0.5f).toInt()
                binding.messageLayoutLeft.layoutParams = params
            }

            if (message.authorAvatar.isNullOrEmpty()) {
                binding.avatarLeft.setImageResource(R.drawable.avatar_default)
                return
            }
            Glide.with(this.itemView.context)
                .load(message.authorAvatar)
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .circleCrop()
                .into(binding.avatarLeft)
        }

        companion object {
            fun from(parent: ViewGroup, inflater: LayoutInflater): UsersMessageViewHolder {
                val binding = ItemLeftMessageBinding.inflate(inflater, parent, false)
                return UsersMessageViewHolder(binding)
            }
        }
    }

    class DateViewHolder(private val binding: ItemDateBinding) : ViewHolder(binding.root) {

        fun bind(dateLabel: ChatComponent.DateLabel) {
            if (dateLabel.date.isEqual(LocalDate.now())) {
                binding.date.text = this.itemView.context.resources.getString(R.string.today)
                return
            }

            val monthName = this.itemView.context.resources.getString(
                monthString[dateLabel.date.monthValue - 1]
            )

            val dateText = "${dateLabel.date.dayOfMonth} $monthName"

            binding.date.text = dateText
        }

        companion object {
            fun from(parent: ViewGroup, inflater: LayoutInflater): DateViewHolder {
                val binding = ItemDateBinding.inflate(inflater, parent, false)
                return DateViewHolder(binding)
            }

            val monthString: List<Int> = listOf(
                R.string.january,
                R.string.february,
                R.string.march,
                R.string.april,
                R.string.may,
                R.string.june,
                R.string.july,
                R.string.august,
                R.string.september,
                R.string.october,
                R.string.november,
                R.string.december
            )
        }
    }

    fun addComponent(component: ChatComponent/*, position: Int*/) {
        componentList.add(component)
        notifyItemInserted(componentList.size - 1)
    }

    fun decreasePadding() {
        componentList.last().smallPadding = true
        notifyItemChanged(componentList.size - 1)
    }

    private companion object {
        const val MY_MESSAGE = 0
        const val USERS_MESSAGE = 1
        const val DATE = 2
        const val SMALL_PADDING = 4
        const val BIG_PADDING = 16

        fun getNameAndTimeString(chatMessage: ChatMessage): String {
            val time = chatMessage.creationDateTime.toLocalTime()
            val minute = if (time.minute < 10) "0${time.minute}" else "${time.minute}"
            return "${chatMessage.authorName} • ${time.hour}:${minute}"
        }
    }
}