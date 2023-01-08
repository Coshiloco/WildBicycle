package com.exmaple.wildbicycle.ui.login

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.managers.DataSource
import com.exmaple.wildbicycle.managers.SHA512
import com.exmaple.wildbicycle.managers.SHA512.SHA512Hash
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.utils.Event
import com.exmaple.wildbicycle.utils.UserNotFoundEmailException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userManager: UserManager,
    private val dataSource: DataSource
) : ViewModel() {

    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage


    private var _resetPassword = MutableLiveData<Event<OptionsResetPassword>>()
    val resetPassword: LiveData<Event<OptionsResetPassword>> = _resetPassword

    fun loginWithPlainPassword(email: String, password: String) {
        dataSource.updateBBDDPassword(password, email)
        login(email, password)
    }


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
                onSuccess = { user ->
                    login(
                        user.email,
                        user.password
                    )
                }, // This password from Firestore is cyphered
                onFailure = { _errorMessage.postValue(Event(it.message ?: "Error")) }
            )
        }
    }

    fun resetPassword(email: String, password: String) {
        userManager.resetPassword(email, password) { result ->
            result.fold(
                onSuccess = {
                    if (it) _resetPassword.postValue(
                        Event(OptionsResetPassword.SendEmail)
                    )
                    else _resetPassword.postValue(
                        Event(OptionsResetPassword.UserNotFoundEmail)
                    )
                },
                onFailure = {
                    _errorMessage.postValue(Event(it.message ?: "Error"))
                }
            )
        }
    }

    enum class Navigate {
        Home, GoBack
    }

    enum class OptionsResetPassword {
        SendEmail,
        UserNotFoundEmail
    }
}