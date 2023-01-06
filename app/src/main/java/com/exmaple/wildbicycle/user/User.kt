package com.exmaple.wildbicycle.user

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


class User @Inject constructor(private val auth: FirebaseAuth) {

    fun checkNotNullUser(callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) callback(true)
        else callback(false)
    }
}