package com.example.asurion_test.repository

import androidx.lifecycle.MutableLiveData
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Config
import com.example.asurion_test.network.response.Pets
import com.example.asurion_test.network.response.Settings
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class HomeRepository {
    private val petLiveData = MutableLiveData<PetModel>()
    private val configLiveData = MutableLiveData<Config>()

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

                val responseString = response.body()!!.string()

                val petsJsonObject = JSONObject(responseString)

                val petList:ArrayList<Pets> = ArrayList();
                val petsModel = PetModel()

                val petJsonArray: JSONArray = petsJsonObject.getJSONArray("pets")

                for (i in 0 until petJsonArray.length()) {
                    val petJsonObject:JSONObject=petJsonArray.getJSONObject(i)
                    val pet = Pets();
                    pet.title=petJsonObject.getString("title")
                    pet.date_added=petJsonObject.getString("date_added")
                    pet.content_url=petJsonObject.getString("content_url")
                    petList.add(pet)
                }
                petsModel.pet=petList

                petLiveData.postValue(petsModel)
            }
        })
        return petLiveData
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
                val responseString = response.body()!!.string()

                val petsJsonObject = JSONObject(responseString)

                val config = Config()
                val settings=Settings()

                val settingJsonObject: JSONObject = petsJsonObject.getJSONObject("settings")

                settings.isCallEnabled=settingJsonObject.getBoolean("isCallEnabled")
                settings.isChatEnabled=settingJsonObject.getBoolean("isChatEnabled")
                settings.workHours=settingJsonObject.getString("workHours")

                config.settings=settings

                configLiveData.postValue(config)
            }
        })
        return settingsLiveData
    }

}
