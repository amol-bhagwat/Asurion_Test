package com.example.asurion_test.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    private var loadBar : ProgressBar? = null
    private var homeViewModel: HomeViewModel? = null
    private var mPetListAdapter: PetListAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mBtnCall: AppCompatButton? = null
    private var mBtnChat: AppCompatButton? = null
    private var mTextViewOfficeTime: TextView? = null
    private var isWithinOffice : Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        preWork()

        if(NetworkUtils.isNetworkAvailable(this)) {
            getPet()
            getConfig()
        }else{
            Toast.makeText(this,getString(R.string.check_internet_connection),Toast.LENGTH_SHORT).show()
        }
    }

    private fun preWork() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mPetListAdapter = PetListAdapter(this)
        mRecyclerView?.adapter = mPetListAdapter
    }

    private fun initViews() {
        mBtnChat=findViewById(R.id.buttonChat)
        mBtnCall=findViewById(R.id.buttonCall)
        mTextViewOfficeTime=findViewById(R.id.textViewOfficeTime)

        mRecyclerView=findViewById(R.id.recyclerView)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setHasFixedSize(true)

        mBtnCall?.setOnClickListener {
            officeStatus()
        }

        mBtnChat?.setOnClickListener {
            officeStatus()
        }
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



    private fun getPet() {
        homeViewModel!!.allPet.observe(this,
            Observer<Any> { mPetModel ->
                    mPetListAdapter?.setPetList(mPetModel as PetModel)
                    loadBar?.visibility = View.GONE
            })
        homeViewModel!!.allPet.observe(this,
            Observer<Any> { mPetModel ->
                mPetListAdapter?.setPetList(mPetModel as PetModel)
                loadBar?.visibility = View.GONE
            })
        homeViewModel!!.allPet.observe(this,
            Observer<Any> { mPetModel ->
                mPetListAdapter?.setPetList(mPetModel as PetModel)
                loadBar?.visibility = View.GONE
            })
    }

    private fun officeStatus() {
        if(isWithinOffice){
            officeWorkAlert(getString(R.string.within_work_hours))
        }else{
            officeWorkAlert(getString(R.string.outside_work_hours))
        }

    }

        private fun getConfig() {
        homeViewModel!!.getConfig.observe(this, Observer { config ->

            val splittedWorkHours = config.settings?.workHours?.split(" ")

            val startTime=parseDate(splittedWorkHours!![1])
            val closingTime=parseDate(splittedWorkHours[3])

            val currentTime = parseDate(SimpleDateFormat("HH.mm", Locale.getDefault()).format(Date()))

            isWithinOffice = currentTime!!.before(closingTime) && currentTime.after(startTime)

            if(config.settings?.isChatEnabled==true){
                mBtnChat?.visibility=View.VISIBLE
            }
            if(config.settings?.isCallEnabled==true){
                mBtnCall?.visibility=View.VISIBLE
            }
            mTextViewOfficeTime?.text = getString(R.string.office_hours)+config.settings?.workHours
        })
    }

    private fun officeWorkAlert(message:String){
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(getString(R.string.alert_msg))
            setMessage(message)
            show()
        }
    }
}
