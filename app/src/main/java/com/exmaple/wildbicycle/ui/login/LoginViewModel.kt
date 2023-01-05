package com.exmaple.wildbicycle.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.database.DataSource
import com.exmaple.wildbicycle.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val database: DataSource
) : ViewModel() {

    private val _navigation = MutableLiveData<Event<Navigate>>()
    val navigation: LiveData<Event<Navigate>> = _navigation

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun googleLogin(user: String, pass: String) {
        if(user.isNotBlank() && pass.isNotBlank()) {
            database.login(user, pass) { result ->
                if (result) {
                    _navigation.postValue(Event(Navigate.HOME))
                } else {
                    _errorMessage.postValue("Login Error")
                }
            }
        }
        else{
            _errorMessage.postValue("Mete los datos payaso!")
        }
    }

    enum class Navigate {
        HOME,
        BACK
    }
}