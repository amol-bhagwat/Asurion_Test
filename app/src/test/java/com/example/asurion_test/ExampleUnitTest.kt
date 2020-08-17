package com.example.asurion_test

import com.example.asurion_test.network.response.Config
import com.example.asurion_test.network.response.Pets
import com.example.asurion_test.network.response.Settings
import com.example.asurion_test.repository.HomeRepository
import com.example.asurion_test.viewmodel.HomeViewModel
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun parseConfigData() {
        val setting = Settings()
        setting.isChatEnabled = false
        setting. isCallEnabled= true
        setting.workHours = "M-F 9:00 - 18:00"
        val homeRepository = HomeRepository()

        val settingsParsed=homeRepository.configParsing("{\"settings\": {\n" +
                "\t\"isChatEnabled\" : false,\n" +
                "\t\"isCallEnabled\" : true,\n" +
                "\t\"workHours\" : \"M-F 9:00 - 18:00\"\n" +
                "}\n" +
                "}")
        assertEquals(settingsParsed, setting)
    }

    @Test
    fun parsePetResponse() {
        val petList: ArrayList<Pets> = ArrayList();

        val pet = Pets();
        pet.content_url = "https://en.wikipedia.org/wiki/Cat"
        pet.title = "Cat"
        pet.image_url =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg"
        pet.date_added = "2018-06-02T03:27:38.027Z"

        petList.add(pet)

        val homeRepository = HomeRepository()
        val settingsParsed = homeRepository.parsePetResponse(
            "{\n" +
                    "\t\"pets\": [{\n" +
                    "\t\t\"image_url\": \"https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg\",\n" +
                    "\t\t\"title\": \"Cat\",\n" +
                    "\t\t\"content_url\": \"https://en.wikipedia.org/wiki/Cat\",\n" +
                    "\t\t\"date_added\": \"2018-06-02T03:27:38.027Z\"\n" +
                    "\t}]\n" +
                    "}"
        )

        assertEquals(settingsParsed, petList)
    }

    @Test
    fun checkDateCompareOutOfOffice() {
        val homeViewModel = HomeViewModel()
        val config= Config()
        config.settings= Settings()
        config.settings?.workHours="M-F 9:00 - 18:00"
        assertFalse(homeViewModel.compareOfficeTime(config, SimpleDateFormat("HH:mm", Locale.US).parse("20:00")))
    }

    @Test
    fun checkDateCompareWithinOffice() {
        val homeViewModel = HomeViewModel()
        val config= Config()
        config.settings= Settings()
        config.settings?.workHours="M-F 9:00 - 18:00"
        assertTrue(homeViewModel.compareOfficeTime(config, SimpleDateFormat("HH:mm", Locale.US).parse("10:00")))
    }
}
