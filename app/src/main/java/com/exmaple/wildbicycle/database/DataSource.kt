package com.exmaple.wildbicycle.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class DataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            login -> if (login.isSuccessful) println("completado")
            else println("Registrarse")
        }
    }


}