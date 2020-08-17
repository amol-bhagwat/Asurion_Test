package com.example.asurion_test.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.example.asurion_test.R
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

    fun getPetList(): MutableLiveData<PetModel> {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(Constant.PET_URL)
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                val petModel = PetModel()
                petModel.error = Resources.getSystem().getString(R.string.something_went_wrong)
                petLiveData.postValue(petModel)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()

                val petModel = PetModel()

                petModel.pet = parsePetResponse(responseString)

                petLiveData.postValue(petModel)
            }
        })
        return petLiveData
    }

    fun parsePetResponse(responseString: String?): ArrayList<Pets> {
        //Parse Data
        val petsJsonObject = JSONObject(responseString)

        val petList: ArrayList<Pets> = ArrayList();

        val petJsonArray: JSONArray = petsJsonObject.getJSONArray("pets")

        for (i in 0 until petJsonArray.length()) {
            val petJsonObject: JSONObject = petJsonArray.getJSONObject(i)
            val pet = Pets();
            pet.title = petJsonObject.getString("title")
            pet.date_added = petJsonObject.getString("date_added")
            pet.content_url = petJsonObject.getString("content_url")
            pet.image_url = petJsonObject.getString("image_url")

            petList.add(pet)
        }
        return petList
    }


    fun getConfig(): MutableLiveData<Config> {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .get()
            .url(Constant.CONFIG_URL)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                val config = Config()
                config.error = Resources.getSystem().getString(R.string.something_went_wrong)
                configLiveData.postValue(config as Config?)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()

                val config = Config()

                config.settings = configParsing(responseString)

                configLiveData.postValue(config)
            }
        })
        return configLiveData
    }

    fun configParsing(responseString: String?): Settings {
        //Parse Data
        val petsJsonObject = JSONObject(responseString)

        val settings = Settings()

        val settingJsonObject: JSONObject = petsJsonObject.getJSONObject("settings")

        settings.isCallEnabled = settingJsonObject.getBoolean("isCallEnabled")
        settings.isChatEnabled = settingJsonObject.getBoolean("isChatEnabled")
        settings.workHours = settingJsonObject.getString("workHours")

        return settings
    }


}
