package com.example.asurion_test.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.repository.HomeRepository


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val mPetRepository: HomeRepository = HomeRepository()
    private val mContext: Context = getApplication<Application>().applicationContext

    val allPet: LiveData<List<PetModel>>
        get() = mPetRepository.getMutableLiveData(mContext)

}
