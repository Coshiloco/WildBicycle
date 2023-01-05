package com.exmaple.wildbicycle.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class DataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                callback(it.isSuccessful && it.result != null)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}
