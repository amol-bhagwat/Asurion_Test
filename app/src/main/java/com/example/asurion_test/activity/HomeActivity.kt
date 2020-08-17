package com.example.asurion_test.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.R
import com.example.asurion_test.adapters.PetListAdapter
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.util.NetworkUtils
import com.example.asurion_test.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private var petListAdapter: PetListAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var btnCall: AppCompatButton? = null
    private var btnChat: AppCompatButton? = null
    private var textViewOfficeTime: TextView? = null
    private var isWithinOfficeHours: Boolean = false
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        preWork()

        //Check internet and dp api calll
        if (NetworkUtils.isNetworkAvailable(this)) {
            getConfig()
            getPet()
        } else {
            Toast.makeText(this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun preWork() {
        petListAdapter = PetListAdapter(this)
        recyclerView?.adapter = petListAdapter
    }

    private fun initViews() {
        btnChat = findViewById(R.id.buttonChat)
        btnCall = findViewById(R.id.buttonCall)
        textViewOfficeTime = findViewById(R.id.textViewOfficeTime)
        progressBar = findViewById(R.id.progressBar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.setHasFixedSize(true)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        btnCall?.setOnClickListener {
            officeStatus()
        }

        btnChat?.setOnClickListener {
            officeStatus()
        }
    }

    //Get pet details
    private fun getPet() {
        progressBar?.visibility = View.VISIBLE

        homeViewModel.allPet.observe(this,
            Observer { petModel ->
                progressBar?.visibility = View.GONE
                if (petModel.error.isNullOrBlank()) {
                    petListAdapter?.setPetList(petModel as PetModel)
                } else {
                    Toast.makeText(this, petModel.error, Toast.LENGTH_SHORT).show()
                }
            })
    }

    //Get configuration
    private fun getConfig() {
        progressBar?.visibility = View.VISIBLE
        homeViewModel.getConfig.observe(this, Observer { config ->
            progressBar?.visibility = View.GONE

            if (config.error.isNullOrBlank()) {
                isWithinOfficeHours =
                    homeViewModel.compareOfficeTime(config, homeViewModel.currentTime())

                if (config.settings?.isChatEnabled == true) {
                    btnChat?.visibility = View.VISIBLE
                }
                if (config.settings?.isCallEnabled == true) {
                    btnCall?.visibility = View.VISIBLE
                }
                textViewOfficeTime?.text =
                    getString(R.string.office_hours).plus(config.settings?.workHours)

            } else {
                Toast.makeText(this, config.error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun officeWorkAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(getString(R.string.alert_msg))
            setMessage(message)
            show()
        }
    }

    //Show alert as per office time
    private fun officeStatus() {
        if (isWithinOfficeHours) {
            officeWorkAlert(getString(R.string.within_work_hours))
        } else {
            officeWorkAlert(getString(R.string.outside_work_hours))
        }
    }

}
