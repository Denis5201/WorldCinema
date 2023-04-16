package com.example.worldcinema.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.worldcinema.data.dto.ChatMessageDto
import com.example.worldcinema.domain.model.ChatMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketListener : WebSocketListener() {

    private val _message = MutableLiveData<ChatMessage>()
    val message: LiveData<ChatMessage> = _message

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.e("setConnection", "==  I'm connect!!!  ==")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.e("ChatWebSocketListener", "$text")
        Handler(Looper.getMainLooper()).post {
            val dto: ChatMessageDto = Json.decodeFromString(text)
            _message.value = dto.toChatMessage()
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.e("ChatWebSocketListener", "close $reason")
        webSocket.close(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("ChatWebSocketListener", "WebSocket onFailure: ${t.message}")
        super.onFailure(webSocket, t, response)
    }
}