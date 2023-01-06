package com.exmaple.wildbicycle.ui.login

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.database.DataSource
import com.exmaple.wildbicycle.utils.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val database: DataSource
) : ViewModel() {

    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate


    private var _register = MutableLiveData<Event<Navigate>>()
    val register: LiveData<Event<Navigate>> = _register



    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun comprobarLogin(email: String, password: String) {
        database.login(email, password) {
            if (it) {
                _navigate.postValue(Event(Navigate.Home))
            } else {
                _errorMessage.postValue(Event("Error en algun campo"))
            }
        }
    }

    fun Registrarse (email: String, password: String) {
        database.registerUser(email, password) {
            if (it) {
                _register.postValue(Event(Navigate.Home))
            } else {
                _errorMessage.postValue(Event("Error en algun campo"))
            }
        }
    }

    enum class Navigate() {
        Home,
        GoBack
    }


}