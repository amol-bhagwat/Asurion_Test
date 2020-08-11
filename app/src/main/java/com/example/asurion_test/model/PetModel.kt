package com.example.asurion_test.model

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.annotations.SerializedName


class PetModel(val login: String, val avatar_url: String) {

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
