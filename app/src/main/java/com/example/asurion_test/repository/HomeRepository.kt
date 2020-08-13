package com.example.asurion_test.repository

import androidx.lifecycle.MutableLiveData
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Config
import com.example.asurion_test.network.response.Pets
import com.example.asurion_test.network.response.Settings
import com.example.asurion_test.util.Constant
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class HomeRepository {
    private val petLiveData = MutableLiveData<PetModel>()
    private val configLiveData = MutableLiveData<Config>()
    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()


    fun getPetLiveData(): MutableLiveData<PetModel> {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(Constant.PET_URL)
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                errorLiveData.postValue("Something went wrong")
                progressLiveData.postValue(false)
            }

            override fun onResponse(call: Call, response: Response) {
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
                    pet.image_url=petJsonObject.getString("image_url")

                    petList.add(pet)
                }
                petsModel.pet=petList

                petLiveData.postValue(petsModel)
            }
        })
        return petLiveData
    }

    fun getConfig(): MutableLiveData<Config> {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(Constant.CONFIG_URL)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                errorLiveData.postValue("Something went wrong")
                progressLiveData.postValue(false)
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
        return configLiveData
    }

}
