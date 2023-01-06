package com.exmaple.wildbicycle.database

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


class DataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) {


    companion object {
        lateinit var providerSession: String
    }

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
            if (login.isSuccessful)  {
                providerSession = "emailAndPassword"
                callback(true)
            }
            else callback(false)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun registerUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                providerSession = "emailAndPassword"
                accederALosUsuariosYRegistro(email, password)
                callback(true)
            }
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun configurarPersistenciaSinConexion(): FirebaseFirestore {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        database.firestoreSettings = settings
        val settings2 = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        database.firestoreSettings = settings2

        return database
    }

    fun accederALosUsuariosYRegistro(email: String, password: String) {
        var dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val database = configurarPersistenciaSinConexion()
        database.collection("users").document(email).set(hashMapOf(
            "user" to email,
            "dateRegister" to dateRegister
        ))
    }

}