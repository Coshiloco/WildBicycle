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

    fun singOutUser(activity: MainActivity, context: Context) {
        userManager.signOut { result ->
            result.fold(
                onSuccess = {
                    if (it) Toast.makeText(
                        context,
                        "Se deslogeo de forma normal con exito ",
                        Toast.LENGTH_LONG
                    ) else Toast.makeText(
                        context,
                        "Risas ",
                        Toast.LENGTH_LONG
                    )
                },
                onFailure = {

                }
            )
        }
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, signInRequest)
        mGoogleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(
                context,
                "El ha compuplido el filtro ",
                Toast.LENGTH_LONG
            )
            _navigate.postValue(Event(Navigate.Login))

        }.addOnFailureListener {
            Toast.makeText(
                context,
                "Fallo ",
                Toast.LENGTH_LONG
            )
        }

    }

    companion object {
        private const val WEB_CLIENT_ID =
            "111012438267-2tlousk8k6u925h5cc6jhbsj8eq7fear.apps.googleusercontent.com"
    }
}