package com.example.worldcinema.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.worldcinema.data.dto.ChatMessageDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketListener : WebSocketListener() {

    private val _message = MutableLiveData<ChatMessageDto>()
    val message: LiveData<ChatMessageDto> = _message

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.e("ChatWebSocketListener", "$text")
        Handler(Looper.getMainLooper()).post {
            _message.value = Json.decodeFromString(text)
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("ChatWebSocketListener", "WebSocket onFailure: ${t.message}")
        super.onFailure(webSocket, t, response)
    }
}