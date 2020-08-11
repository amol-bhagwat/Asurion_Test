package com.example.asurion_test.model

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.asurion_test.network.response.Pets


class PetModel{

    var pet: List<Pets> = ArrayList<Pets>()

    companion object  {
        @JvmStatic
        @BindingAdapter("avatar_url")
        fun loadImage(imageView: ImageView, imageURL: String) {

            Log.e("imsgeurl",imageURL)
            Glide.with(imageView.context)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .circleCrop()
                )
                .load(imageURL)

                .into(imageView)
        }

    }

}
