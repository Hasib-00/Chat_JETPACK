package com.example.chat.auth

import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Register user
     */
    suspend fun signUp(email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return false

            val userData = UserModel(
                id = firebaseUser.uid,
                email = email
            )

            db.collection("users")
                .document(firebaseUser.uid)
                .set(userData)
                .await()

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Login user
     */
    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
