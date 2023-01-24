package com.exmaple.wildbicycle.ui.record

import androidx.lifecycle.ViewModel
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.managers.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val userManager: UserManager,
) : BaseViewModel() {

}