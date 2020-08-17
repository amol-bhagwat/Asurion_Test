package com.example.asurion_test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Config
import com.example.asurion_test.repository.HomeRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val petRepository: HomeRepository = HomeRepository()

    val allPet: LiveData<PetModel>
        get() = petRepository.getPetList()

    val getConfig: LiveData<Config>
        get() = petRepository.getConfig()

    //Compare office time slot
    fun compareOfficeTime(config: Config?, currentTime: Date?): Boolean {

        //Split work hour string and get office start time and closing time
        val splittedWorkHours = config?.settings?.workHours?.split(" ")

        val startTime = SimpleDateFormat("HH:mm", Locale.US).parse(splittedWorkHours!![1])
        val closingTime = SimpleDateFormat("HH:mm", Locale.US).parse(splittedWorkHours[3])

        return currentTime!!.before(closingTime) && currentTime.after(startTime)
    }


    fun currentTime(): Date? {
        return parseDate(
            SimpleDateFormat("HH.mm", Locale.getDefault()).format(
                Date()
            )
        )
    }

    private fun parseDate(date: String): Date? {
        val inputFormat = "HH.mm"
        val inputParser = SimpleDateFormat(inputFormat, Locale.US)
        return try {
            inputParser.parse(date)
        } catch (e: ParseException) {
            Date(0)
        }
    }
}
