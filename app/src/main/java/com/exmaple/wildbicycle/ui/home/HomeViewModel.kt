package com.exmaple.wildbicycle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.managers.UserManager
import com.exmaple.wildbicycle.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate

    private var _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage


    enum class Navigate() {
        GoNext
    }
}