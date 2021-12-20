package com.khs.riotapiproject.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khs.riotapiproject.viewmodel.repository.MainRepository

class MainRepositoryViewModelFactory(private val mainRepository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MainRepository::class.java).newInstance(mainRepository)
    }
}