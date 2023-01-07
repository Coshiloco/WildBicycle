package com.exmaple.wildbicycle.managers

import com.exmaple.wildbicycle.managers.SHA512.SHA512Hash
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.model.User
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import javax.inject.Inject

class UserManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataSource: DataSource,
) {

    // TODO: Handle result instead of boolean
    fun login(email: String, password: String, callback: (Result<Boolean>) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
                if (login.isSuccessful)
                    callback(Result.success(true))
                else
                    callback(Result.success(false))
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        } catch (ex: Exception) {
            callback(Result.failure(ex))
        }
    }

    fun registerUser(
        email: String,
        password: String,
        provider: ProviderType,
        callback: (Result<Boolean>) -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password.SHA512Hash()).addOnCompleteListener {
                if (it.isSuccessful) {
                    User(
                        id = it.result.user?.uid ?: "null",
                        email = email,
                        password = password.SHA512Hash(),
                        provider = provider,
                    ).let { user ->
                        dataSource.registerNewUser(user)
                        callback(Result.success(true))
                    }
                }
            }.addOnFailureListener {
                callback(Result.failure(it))
            }
        } catch (ex: Exception) {
            callback(Result.failure(ex))
        }
    }

    /**
     * This method returns the User from firestore knowing the ID
     *
     * @param callback Result<User> obtained from Firebase Auth and stored in Firestore
     */
    fun getUser(callback: (Result<User>) -> Unit) {
        dataSource.getUser(auth.currentUser?.uid ?: "null") {
            callback(it)
        }
    }
}
