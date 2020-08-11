package com.example.asurion_test.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.asurion_test.model.PetModel
import okhttp3.*
import java.io.IOException
import java.util.*


class HomeRepository {
    private val mmDeveloperModelmist = ArrayList<PetModel>()
    private val mutableLiveData = MutableLiveData<List<PetModel>>()

    ////call to internet and  retun  MutableLiveData
    fun getMutableLiveData(context: Context): MutableLiveData<List<PetModel>> {


        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url("https://api.github.com/users")
            .build()

        val users = ArrayList<PetModel>()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this

                val myResponse = response.body()!!.string()


                //adding some dummy data to the list
                users.add(PetModel("Belal Khan", "Ranchi Jharkhand"))
                users.add(PetModel("Ramiz Khan", "Ranchi Jharkhand"))
                users.add(PetModel("Faiz Khan", "Ranchi Jharkhand"))
                users.add(PetModel("Yashar Khan", "Ranchi Jharkhand"))

                mutableLiveData.postValue(users)
            }
        })


        return mutableLiveData
    }

}
