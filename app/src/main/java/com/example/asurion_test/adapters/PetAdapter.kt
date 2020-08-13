package com.example.asurion_test.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.asurion_test.HomeActivity
import com.example.asurion_test.PetInfoActivity
import com.example.asurion_test.R
import com.example.asurion_test.model.PetModel
import com.example.asurion_test.network.response.Pets
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


class PetAdapter(mContext: Context?) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    private var petModel: PetModel? = PetModel()

    var context : Context?=null

    init {
        this.context = mContext
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.pet_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(petModel?.pet!!.get(position))
    }

    override fun getItemCount(): Int {
        return petModel?.pet!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pet: Pets) {
            val textViewName = itemView.findViewById(R.id.textViewPetName) as TextView
            val imageView = itemView.findViewById(R.id.imageViewPet) as ImageView

             BitmapWorkerTask(imageView).execute(pet.image_url);
            textViewName.setText(pet.title)

            itemView.setOnClickListener {
                val intent = Intent(context as HomeActivity, PetInfoActivity::class.java)
                intent.putExtra("URL", pet.content_url)
                (context as HomeActivity).startActivity(intent)
            }
        }
    }

    fun setPetList(mPetModel:PetModel) {
        this.petModel = mPetModel
        notifyDataSetChanged()
    }

    internal class BitmapWorkerTask(imageView: ImageView?) :
        AsyncTask<String?, Void?, Bitmap?>() {
        private val imageViewReference: WeakReference<ImageView>?
        private var imageUrl: String? = null

        // Once complete, see if ImageView is still around and set bitmap.
        override fun onPostExecute(bitmap: Bitmap?) {
            if (imageViewReference != null && bitmap != null) {
                val imageView: ImageView? = imageViewReference.get()
                imageView?.setImageBitmap(bitmap)
            }
        }

        private fun LoadImage(URL: String?): Bitmap? {
            var bitmap: Bitmap? = null
            var `in`: InputStream? = null
            try {
                `in` = OpenHttpConnection(URL)
                bitmap = BitmapFactory.decodeStream(`in`)
                `in`!!.close()
            } catch (e1: IOException) {
            }
            return bitmap
        }

        @Throws(IOException::class)
        private fun OpenHttpConnection(strURL: String?): InputStream? {
            var inputStream: InputStream? = null
            val url = URL(strURL)
            val conn = url.openConnection()
            try {
                val httpConn = conn as HttpURLConnection
                httpConn.requestMethod = "GET"
                httpConn.connect()
                if (httpConn.responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.inputStream
                }
            } catch (ex: Exception) {
            }
            return inputStream
        }

        init { // Use a WeakReference to ensure the ImageView can be garbage
// collected
            imageViewReference = WeakReference<ImageView>(imageView)
        }

        override fun doInBackground(vararg params: String?): Bitmap? {
            imageUrl = params[0]
            return LoadImage(imageUrl)        }
    }
}
