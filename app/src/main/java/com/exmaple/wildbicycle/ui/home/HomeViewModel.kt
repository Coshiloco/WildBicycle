package com.exmaple.wildbicycle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.utils.Event

class HomeViewModel : ViewModel() {


    private var _navigate = MutableLiveData<Event<Navigate>>()
    val navigate: LiveData<Event<Navigate>> = _navigate


    enum class Navigate() {
        GoNext
    }
}