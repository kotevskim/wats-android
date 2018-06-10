package com.kote.martin.wats.presentation

import android.arch.lifecycle.ViewModel
import android.app.Application
import android.arch.lifecycle.ViewModelProvider


class MyViewModelFactory(private val mApplication: Application, private val locationId: Long) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(mApplication, locationId) as T
    }
}