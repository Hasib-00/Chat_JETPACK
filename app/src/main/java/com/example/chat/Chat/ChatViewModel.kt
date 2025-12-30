package com.example.chat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.MessageModel
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // All messages in this chat
    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> get() = _messages

    // Opponent user (the person you are chatting with)
    private val _chatUser = MutableStateFlow<UserModel?>(null)
    val chatUser: StateFlow<UserModel?> get() = _chatUser

    private var messagesListener: ListenerRegistration? = null

    /**
     * Load opponent user info using Firestore "users" collection
     */
    fun loadChatUserInfo(userId: String) {
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    _chatUser.value = snapshot.toObject(UserModel::class.java)
                }
            }
    }

    /**
     * Listen to messages in real-time from "messages" collection
     * Only messages between current user and [userId]
     */
    fun loadMessages(userId: String) {
        val currentUserId = auth.currentUser?.uid ?: return

        // remove old listener to avoid duplicates
        messagesListener?.remove()

        messagesListener = db.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val allMessages = snapshot?.documents?.mapNotNull {
                    it.toObject(MessageModel::class.java)
                } ?: emptyList()

                val filtered = allMessages.filter { msg ->
                    (msg.senderId == currentUserId && msg.receiverId == userId) ||
                            (msg.senderId == userId && msg.receiverId == currentUserId)
                }

                _messages.value = filtered
            }
    }

    /**
     * Send a message to [receiverId]
     */
    fun sendMessage(receiverId: String, message: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        if (message.isBlank()) return

        val msg = MessageModel(
            senderId = currentUserId,
            receiverId = receiverId,
            message = message,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            db.collection("messages").add(msg)
        }
    }

    override fun onCleared() {
        super.onCleared()
        messagesListener?.remove()
    }
}
