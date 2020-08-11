package com.example.asurion_test.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.R
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Pets

class PetAdapter : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    private var petModel: PetModel? = PetModel()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(petModel?.pet!!.get(position))
    }

    override fun getItemCount(): Int {
        return petModel?.pet!!.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pet: Pets) {
            val textViewName = itemView.findViewById(R.id.textViewUsername) as TextView
            val textViewAddress  = itemView.findViewById(R.id.textViewAddress) as TextView
            textViewName.text = pet.title
            textViewAddress.text = pet.date_added
        }
    }

    fun setPetList(mPetModel:PetModel) {
        this.petModel = mPetModel
        notifyDataSetChanged()
    }
}
