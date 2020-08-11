package com.example.asurion_test.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Config
import com.example.asurion_test.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val mPetRepository: HomeRepository = HomeRepository()

    val allPet: LiveData<PetModel>
        get() = mPetRepository.getMutableLiveData()


    val config: LiveData<Config>
        get() = mPetRepository.getConfig()
}
