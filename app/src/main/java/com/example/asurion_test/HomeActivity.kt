package com.example.asurion_test

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.adapters.PetAdapter
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.viewmodel.HomeViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    internal var loadBar : ProgressBar? = null
    var homeViewModel: HomeViewModel? = null
    private var mPetAdapter: PetAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mBtnCall: AppCompatButton? = null
    private var mBtnChat: AppCompatButton? = null
    private var mTextViewOfficeTime: TextView? = null
    var isWithinOffice : Boolean =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val calendar: Calendar = Calendar.getInstance()
//        val hourOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY)
//        val minutes: Int  = calendar[Calendar.MINUTE]
//
//        val splittedStringList = "M/F 10.00 - 18.00".split(" ")
//        println(splittedStringList[0])
//
//        val startTime=parseDate(splittedStringList[1])
//        val closingTime=parseDate(splittedStringList[3])
//
//        val currentTime =
//            parseDate(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
//
//        if (closingTime!!.before(currentTime)) {
//            Log.e("Time :", "===> is before from current time")
//        }
//
//        if (startTime!!.after(currentTime)) {
//            Log.e("Time :", "===> is after from current time")
//        }

        mBtnChat=findViewById(R.id.buttonChat)
        mBtnChat=findViewById(R.id.buttonCall)
        mTextViewOfficeTime=findViewById(R.id.textViewOfficeTime)

        mRecyclerView=findViewById(R.id.recyclerView)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setHasFixedSize(true)

        ///init the View Model
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //init the Custom adapter
        mPetAdapter = PetAdapter(this)
        //mRecyclerView the CustomAdapter
        mRecyclerView?.setAdapter(mPetAdapter)

        mBtnCall?.setOnClickListener {
            officeStatus()
        }

        mBtnChat?.setOnClickListener {
            officeStatus()
        }

        getPet()
        getConfig()
        //basicAlert()
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
                // if any thing change the update the UI
                    mPetAdapter?.setPetList(mPetModel as PetModel)
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
            mTextViewOfficeTime?.setText(config.settings?.workHours)
        })
    }

    fun officeWorkAlert(message:String){
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Alert")
            setMessage(message)
            show()
        }


    }
}
