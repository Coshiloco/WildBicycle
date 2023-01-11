package com.exmaple.wildbicycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.ui.login.LoginViewModel
import com.exmaple.wildbicycle.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseViewModel() {

    fun singOutUser() {
        userManager.signOut { result ->
            result.fold(
                onSuccess = {
                    _navigate.postValue(Event(Navigate.Login))
                },
                onFailure = {
                    _errorMessage.postValue(Event("Error deslogear"))
                }
            )
        }
    }
}