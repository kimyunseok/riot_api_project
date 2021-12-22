package com.khs.riotapiproject.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khs.riotapiproject.viewmodel.repository.MyRepository

class MyRepositoryViewModelFactory(private val mainRepository: MyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MyRepository::class.java).newInstance(mainRepository)
    }
}