package com.example.chat.auth

import com.example.chat.model.MessageModel
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

/**
 * Handles Authentication + Firebase Chat features in one place
 */
class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // This keeps track of the latest error message for the UI to display
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError

    // =======================================================
    // 🔹 AUTH FUNCTIONS
    // =======================================================

    /**
     * Create new user with email + password
     * Save additional fields (username, email) in Firestore
     */
    suspend fun signUp(email: String, password: String, username: String?): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User creation failed")

            // If username empty -> use email prefix
            val finalUsername = username?.ifBlank { email.substringBefore("@") }
                ?: email.substringBefore("@")

            val userData = UserModel(
                id = firebaseUser.uid,
                email = email,
                username = finalUsername
            )

            // Save user profile in Firestore
            db.collection("users")
                .document(firebaseUser.uid)
                .set(userData)
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            _authError.value = e.message
            false
        }
    }

    /**
     * Login with email and password
     */
    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            _authError.value = e.message
            false
        }
    }

    /**
     * Fetch current logged-in user's profile from Firestore
     */
    suspend fun getCurrentUser(): UserModel? {
        val uid = getCurrentUserId() ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(UserModel::class.java)
    }

    /**
     * Get Firebase UID directly
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Logout user from Firebase
     */
    fun signOut() = auth.signOut()

    // =======================================================
    // 💬 CHAT FUNCTIONS
    // =======================================================

    /**
     * Create a unique chat room ID using two User IDs
     * Sorting ensures same room for both sides
     */
    private fun chatId(user1: String, user2: String): String {
        return if (user1 < user2) "$user1-$user2" else "$user2-$user1"
    }

    /**
     * Listen to messages in realtime using Firestore Snapshot Listener
     */
    fun listenMessages(receiverId: String): StateFlow<List<MessageModel>> {
        val senderId = getCurrentUserId() ?: ""
        val room = chatId(senderId, receiverId)

        val flow = MutableStateFlow<List<MessageModel>>(emptyList())

        db.collection("chats")
            .document(room)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    flow.value = snapshot.toObjects(MessageModel::class.java)
                }
            }

        return flow
    }

    /**
     * Send new message to Firestore
     */
    suspend fun sendMessage(receiverId: String, text: String): Boolean {
        val senderId = getCurrentUserId() ?: return false
        val room = chatId(senderId, receiverId)

        return try {
            val id = db.collection("chats")
                .document(room)
                .collection("messages")
                .document().id

            val message = MessageModel(
                id = id,
                senderId = senderId,
                receiverId = receiverId,
                message = text,
                timestamp = System.currentTimeMillis()
            )

            db.collection("chats")
                .document(room)
                .collection("messages")
                .document(id)
                .set(message)
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
