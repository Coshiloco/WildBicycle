package com.exmaple.wildbicycle.managers

import android.text.TextUtils
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.model.User
import com.exmaple.wildbicycle.utils.UserEmailNotIntroducingException
import com.exmaple.wildbicycle.utils.UserErrorLoginException
import com.google.firebase.auth.FirebaseAuth
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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        User(
                            id = it.result.user?.uid ?: "null",
                            email = email,
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
     * THis method do a signOut
     */
    fun signOut(callback: (Result<Boolean>) -> Unit) {
        auth.signOut()
        callback(Result.success(true))
    }

    fun autoLogin(callback: (Result<Boolean>) -> Unit) {
        if (auth.currentUser != null) callback(Result.success(true))
        else callback(Result.failure(UserErrorLoginException()))
    }

    /**
     * This method sent a email to reset the password to he user
     */
    fun resetPassword(email: String, callback: (Result<Boolean>) -> Unit) {
        if (!TextUtils.isEmpty(email)) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.success(true))
                } else callback(Result.success(false))
            }
        } else {
            callback(Result.failure(UserEmailNotIntroducingException()))
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
