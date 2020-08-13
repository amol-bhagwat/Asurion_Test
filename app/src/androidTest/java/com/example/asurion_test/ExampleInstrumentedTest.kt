package com.example.asurion_test

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asurion_test.network.response.Config
import com.example.asurion_test.network.response.Settings
import com.example.asurion_test.viewmodel.HomeViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.asurion_test", appContext.packageName)
    }

    @Test
    fun checkDateCompareOutOfOffice() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val homeViewModel = HomeViewModel(appContext as Application)
        val config= Config()
        config.settings= Settings()
        config.settings?.workHours="M-F 9:00 - 18:00"
        assertFalse(homeViewModel.compareOfficeTime(config, SimpleDateFormat("HH:mm", Locale.US).parse("20:00")))
    }

    @Test
    fun checkDateCompareWithinOffice() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val homeViewModel = HomeViewModel(appContext as Application)
        val config= Config()
        config.settings= Settings()
        config.settings?.workHours="M-F 9:00 - 18:00"
        assertTrue(homeViewModel.compareOfficeTime(config, SimpleDateFormat("HH:mm", Locale.US).parse("10:00")))
    }
}
