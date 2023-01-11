package com.exmaple.wildbicycle.bases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.utils.Event

@Suppress("PropertyName")
open class BaseViewModel : ViewModel() {

    var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    var _progress = MutableLiveData<Event<Boolean>>()
    val progress: LiveData<Event<Boolean>> = _progress

    enum class Navigate {
        Login,
        Home
    }
}