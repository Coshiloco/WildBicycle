package com.exmaple.wildbicycle.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.database.DataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val database: DataSource
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val _text = MutableLiveData<String>().apply {
        value = "This is the login fragment"
    }





    val text: LiveData<String> = _text
}