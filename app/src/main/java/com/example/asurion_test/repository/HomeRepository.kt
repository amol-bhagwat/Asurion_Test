package com.example.asurion_test.repository

import androidx.lifecycle.MutableLiveData
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Config
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class HomeRepository {
    private val mutableLiveData = MutableLiveData<PetModel>()

    fun getMutableLiveData(): MutableLiveData<PetModel> {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url("https://raw.githubusercontent.com/amol-bhagwat/Asurion_Test/master/app/src/main/res/assets/pets.json")
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this

                val myResponse = response.body()!!.string()

                val  pets= Gson().fromJson(myResponse, PetModel::class.java)

                mutableLiveData.postValue(pets)
            }
        })


        return mutableLiveData
    }

    fun getConfig(): MutableLiveData<Config> {
         val settingsLiveData = MutableLiveData<Config>()

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url("https://raw.githubusercontent.com/amol-bhagwat/Asurion_Test/master/app/src/main/res/assets/config.json")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                val myResponse = response.body()!!.string()

                val  config= Gson().fromJson(myResponse, Config::class.java)

                settingsLiveData.postValue(config)
            }
        })


        return settingsLiveData
    }

}
