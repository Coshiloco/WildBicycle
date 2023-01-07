package com.exmaple.wildbicycle.ui.login

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.managers.SHA512
import com.exmaple.wildbicycle.managers.SHA512.SHA512Hash
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.utils.Event
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {

    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun loginWithPlainPassword(email: String, password: String) =
        login(email, password.SHA512Hash())

    fun login(email: String, password: String) {
        userManager.login(email, password) { result ->
            result.fold(onSuccess = {
                if (it) _navigate.postValue(Event(Navigate.Home))
                else _errorMessage.postValue(Event("Login false"))

            }, onFailure = { error ->
                _errorMessage.postValue(Event(error.message ?: "Error en algun campo"))
            })
        }
    }

    fun registerEmailUser(email: String, password: String) {
        userManager.registerUser(email, password, ProviderType.EMAIL_PASS) { result ->
            result.fold(
                onSuccess = { _navigate.postValue(Event(Navigate.Home)) },
                onFailure = { _errorMessage.postValue(Event("Error en algun campo")) }
            )
        }
    }

    fun tryLoginExistingUser() {
        userManager.getUser() { result ->
            result.fold(
                onSuccess = { user -> login(user.email, user.password) }, // This password from Firestore is cyphered
                onFailure = { _errorMessage.postValue(Event(it.message ?: "Error")) }
            )
        }
    }

    enum class Navigate {
        Home, GoBack
    }
}