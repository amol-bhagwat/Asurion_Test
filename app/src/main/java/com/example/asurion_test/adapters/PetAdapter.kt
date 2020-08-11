package com.example.asurion_test.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.R
import com.example.asurion_test.model.PetModel
import java.util.ArrayList

class PetAdapter() : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    private var petList: ArrayList<PetModel>? = ArrayList<PetModel>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(petList!![position])
    }

    override fun getItemCount(): Int {
        return petList!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: PetModel) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView
            textViewName.text = user.login
            textViewAddress.text = user.login
        }
    }

    fun setPetList(mPetModel: ArrayList<PetModel>) {
        this.petList = mPetModel
        notifyDataSetChanged()
    }
}
