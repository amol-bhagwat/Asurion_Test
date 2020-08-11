package com.example.asurion_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.adapters.PetAdapter
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.viewmodel.HomeViewModel


class HomeActivity : AppCompatActivity() {

    internal var loadBar : ProgressBar? = null
    var homeViewModel: HomeViewModel? = null
    private var mPetAdapter: PetAdapter? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView=findViewById(R.id.recyclerView)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setHasFixedSize(true)

        ///init the View Model
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        //init the Custom adapter
        mPetAdapter = PetAdapter()
        //mRecyclerView the CustomAdapter
        mRecyclerView?.setAdapter(mPetAdapter)

        getPet()
    }

    private fun getPet() {
        //get the list of pet from api response
        homeViewModel!!.allPet.observe(this,
            Observer<Any> { mPetModel ->
                // if any thing change the update the UI
                    mPetAdapter?.setPetList(mPetModel as PetModel)
                    loadBar?.visibility = View.GONE

            })
    }
}
