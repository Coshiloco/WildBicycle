package com.exmaple.wildbicycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.ui.login.LoginViewModel
import com.exmaple.wildbicycle.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {

    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

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


    enum class Navigate {
        Login, GoBack
    }

}