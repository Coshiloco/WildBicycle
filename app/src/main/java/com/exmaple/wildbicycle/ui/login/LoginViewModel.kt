package com.exmaple.wildbicycle.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.model.ProviderType
import com.exmaple.wildbicycle.utils.Event
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userManager: UserManager,
) : BaseViewModel() {

    private var _validateEmailFormat = MutableLiveData<Event<EmailFormat>>()
    val validateEmailFormat: LiveData<Event<EmailFormat>> = _validateEmailFormat

    private var _resetPassword = MutableLiveData<Event<OptionsResetPassword>>()
    val resetPassword: LiveData<Event<OptionsResetPassword>> = _resetPassword

    fun login(email: String, password: String) {
        _progress.postValue(Event(true))
        userManager.login(email, password) { result ->
            result.fold(
                onSuccess = {
                    if (it) _navigate.postValue(Event(Navigate.Home))
                    else _errorMessage.postValue(Event("Login false"))
                    _progress.postValue(Event(false))
                }, onFailure = { error ->
                    _errorMessage.postValue(Event(error.message ?: "Error en algun campo"))
                    _progress.postValue(Event(false))
                }
            )
        }
    }

    fun validateEmail(email: String) {
        _progress.postValue(Event(true))
        userManager.validateEmailFormat(email) { result ->
            result.fold(
                onSuccess = {
                    if (it) _validateEmailFormat.postValue(Event(EmailFormat.CorrectFormat))
                    else _validateEmailFormat.postValue(Event(EmailFormat.IncorrectFormat))
                    _progress.postValue(Event(false))
                },
                onFailure = { error ->
                    _errorMessage.postValue(Event(error.message ?: "Error en algun campo"))
                    _progress.postValue(Event(false))
                }
            )
        }
    }

    fun registerEmailUser(email: String, password: String) {
        _progress.postValue(Event(true))
        userManager.registerUser(email, password, ProviderType.EMAIL_PASS) { result ->
            result.fold(
                onSuccess = {
                    _navigate.postValue(Event(Navigate.Home))
                    _progress.postValue(Event(false))
                },
                onFailure = {
                    _errorMessage.postValue(Event("Error en algun campo"))
                    _progress.postValue(Event(false))
                }
            )
        }
    }

    fun tryLoginExistingUser() {
        _progress.postValue(Event(true))
        userManager.autoLogin { result ->
            result.fold(
                onSuccess = {
                    _navigate.postValue(Event(Navigate.Home))
                    _progress.postValue(Event(false))
                },
                onFailure = { error ->
                    _errorMessage.postValue(Event(error.message ?: "Error en algun campo"))
                    _progress.postValue(Event(false))
                }
            )
        }
    }

    fun resetPassword(email: String, password: String) {
        userManager.resetPassword(email) { result ->
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

    fun googleLogin(account: GoogleSignInAccount, callback: (Result<Boolean>) -> Unit) {
        userManager.googleLogin(account, { resultActionNavigate ->
            resultActionNavigate.fold(
                onSuccess = {
                    if (it) _navigate.postValue(Event(Navigate.Home))
                },
                onFailure = {

                }
            )
        }, { resultBBDDRegister ->
            resultBBDDRegister.fold(
                onSuccess = {
                    if (it) callback(Result.success(true))
                    else callback(Result.success(false))
                },
                onFailure = {

                }
            )
        })
    }

    enum class OptionsResetPassword {
        SendEmail,
        UserNotFoundEmail
    }

    enum class EmailFormat {
        IncorrectFormat,
        CorrectFormat
    }
}