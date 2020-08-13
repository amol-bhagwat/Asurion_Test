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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    private var mHomeViewModel: HomeViewModel? = null
    private var mPetListAdapter: PetListAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mBtnCall: AppCompatButton? = null
    private var mBtnChat: AppCompatButton? = null
    private var mTextViewOfficeTime: TextView? = null
    private var isWithinOffice : Boolean =false
    private var mProgressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        preWork()

        if(NetworkUtils.isNetworkAvailable(this)) {
            getConfig()
            getPet()
        }else{
            Toast.makeText(this,getString(R.string.check_internet_connection),Toast.LENGTH_SHORT).show()
        }
    }

    private fun preWork() {
        mHomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mPetListAdapter = PetListAdapter(this)
        mRecyclerView?.adapter = mPetListAdapter
    }

    private fun initViews() {
        mBtnChat=findViewById(R.id.buttonChat)
        mBtnCall=findViewById(R.id.buttonCall)
        mTextViewOfficeTime=findViewById(R.id.textViewOfficeTime)
        mProgressBar=findViewById(R.id.progressBar)


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
        mProgressBar?.visibility = View.VISIBLE

        mHomeViewModel!!.allPet.observe(this,
            Observer { petModel ->
                mProgressBar?.visibility = View.GONE
                if(petModel.error.isNullOrBlank()){
                    mPetListAdapter?.setPetList(petModel as PetModel)
                }else{
                    Toast.makeText(this,petModel.error,Toast.LENGTH_SHORT).show()
                }
            })
    }

        private fun getConfig() {
            mProgressBar?.visibility = View.VISIBLE
            mHomeViewModel!!.getConfig.observe(this, Observer { config ->
            mProgressBar?.visibility = View.GONE

                if(config.error.isNullOrBlank()){
                val splittedWorkHours = config.settings?.workHours?.split(" ")

                    val startTime=SimpleDateFormat("HH:mm", Locale.US).parse(splittedWorkHours!![1])
                    val closingTime=SimpleDateFormat("HH:mm", Locale.US).parse("20:00")

                    //val startTime=parseDate(splittedWorkHours!![1])
                //val closingTime=parseDate(splittedWorkHours[3])

                val currentTime = parseDate(SimpleDateFormat("HH.mm", Locale.getDefault()).format(Date()))

                isWithinOffice = currentTime!!.before(closingTime) && currentTime.after(startTime)

                if(config.settings?.isChatEnabled==true){
                    mBtnChat?.visibility=View.VISIBLE
                }
                if(config.settings?.isCallEnabled==true){
                    mBtnCall?.visibility=View.VISIBLE
                }
                mTextViewOfficeTime?.setText(getString(R.string.office_hours)+config.settings?.workHours)

            }else{
                Toast.makeText(this,config.error,Toast.LENGTH_SHORT).show()
            }

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

    private fun officeStatus() {
        if(isWithinOffice){
            officeWorkAlert(getString(R.string.within_work_hours))
        }else{
            officeWorkAlert(getString(R.string.outside_work_hours))
        }
    }

}
