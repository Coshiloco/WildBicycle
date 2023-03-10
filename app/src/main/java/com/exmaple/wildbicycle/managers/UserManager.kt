package com.exmaple.wildbicycle.managers

import android.text.TextUtils
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.model.User
import com.exmaple.wildbicycle.utils.UserEmailNotIntroducingException
import com.exmaple.wildbicycle.utils.UserErrorLoginException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
     * This method validate that email has the correct format
     *
     * @param email String to validate
     */

    fun validateEmailFormat(email: String, callback: (Result<Boolean>) -> Unit) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) callback(
            Result.success(
                true
            )
        )
        else callback(Result.success(false))
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

    fun googleLogin(
        account: GoogleSignInAccount,
        callback: (Result<Boolean>) -> Unit,
        callbackBBDD: (Result<Boolean>) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                User(
                    id = it.user?.uid ?: "null",
                    email = it.user?.email ?: "null",
                    provider = ProviderType.GOOGLE,
                ).let { user ->
                    dataSource.checkSingInGoogleRegisteredInBBDD(account.email) { result ->
                        result.fold(
                            onSuccess = { resultReadUserIFitRegistered ->
                                if (resultReadUserIFitRegistered) {
                                    dataSource.registerNewUser(user)
                                    callbackBBDD(Result.success(true))
                                } else callbackBBDD(Result.success(false))
                            },
                            onFailure = {
                            }
                        )
                    }
                    callback(Result.success(true))
                }
            }
            .addOnFailureListener {
                callback(Result.failure(Exception("Google Login Exception")))
            }
    }
}
