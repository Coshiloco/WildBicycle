package com.exmaple.wildbicycle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.utils.Event
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {


    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage


    fun signOut() {
        userManager.signOut { result ->
            result.fold(
                onSuccess = {
                    if (it) _navigate.postValue(Event(Navigate.GoLogin))
                },
                onFailure = { error ->
                    _errorMessage.postValue(
                        Event(
                            error.message ?: "Error al intentar iniciar sesion"
                        )
                    )
                }
            )
        }
    }


    enum class Navigate() {
        GoLogin
    }
}