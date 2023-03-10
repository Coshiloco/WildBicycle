package com.exmaple.wildbicycle.managers

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.exmaple.wildbicycle.managers.SHA512.SHA512Hash
import com.exmaple.wildbicycle.model.User
import com.exmaple.wildbicycle.utils.UserNotFoundException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.lang.NullPointerException
import java.util.Date
import javax.inject.Inject


class DataSource @Inject constructor(
    private val database: FirebaseFirestore,
) {

    /**
     * Crear un usuario con email/pass en firebase
     */
    fun registerNewUser(user: User) {

        database.collection(USERS_COLLECTION).document(user.id).set(
            hashMapOf(
                "id" to user.id,
                "email" to user.email,
                "provider" to user.provider,
                "date" to user.date,
            )
        )
    }

    fun checkSingInGoogleRegisteredInBBDD(email: String?, callback: (Result<Boolean>) -> Unit) {
        database.collection(USERS_COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                callback(Result.success(false))
            }
            .addOnFailureListener { exception ->
                callback(Result.success(true))
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getUser(userId: String, callback: (Result<User>) -> Unit) {
        database.collection(USERS_COLLECTION).document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                documentSnapshot.toObject<User>().let { user ->
                    if (user != null)
                        callback(Result.success(user))
                    else
                        callback(Result.failure(UserNotFoundException()))
                }

            }
            .addOnFailureListener {
                callback(Result.failure(it))
            }
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}
