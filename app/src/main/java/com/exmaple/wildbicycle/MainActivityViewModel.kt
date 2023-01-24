package com.exmaple.wildbicycle

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.ui.login.LoginFragment
import com.exmaple.wildbicycle.ui.login.LoginViewModel
import com.exmaple.wildbicycle.utils.Event
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseViewModel() {


    private val signInRequest by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    fun singOutUser(
        activity: MainActivity,
        callback: (Result<Boolean>) -> Unit,
        callbackGoogle: (Result<Boolean>) -> Unit
    ) {
        userManager.signOut { result ->
            result.fold(
                onSuccess = {
                    if (it) callback(Result.success(true))
                },
                onFailure = {
                    callback(Result.success(false))
                }
            )
        }
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, signInRequest)
        mGoogleSignInClient.signOut().addOnCompleteListener {

            callbackGoogle(Result.success(true))
            _navigate.postValue(Event(Navigate.Login))

        }.addOnFailureListener {
            callbackGoogle(Result.success(false))
        }

    }

    fun checkLoginToNavigateRecord() {
        userManager.getUser { result ->
            result.fold(
                onSuccess = {
                    _navigate.postValue(Event(Navigate.Record))
                },
                onFailure = {
                    _errorMessage.postValue(Event(it.message ?: "Error en algun campo"))
                }
            )
        }

    }

    companion object {
        private const val WEB_CLIENT_ID =
            "111012438267-2tlousk8k6u925h5cc6jhbsj8eq7fear.apps.googleusercontent.com"
    }
}