package com.example.asurion_test.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.asurion_test.R
import com.example.asurion_test.util.Constant

class PetInfoActivity : AppCompatActivity() {

    private lateinit var url: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_info)

        url = intent.getStringExtra(Constant.URL)

        val view: WebView = findViewById(R.id.webView)
        loadUrl(view);
    }

    private fun loadUrl(view: WebView) {
        view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        view.settings.javaScriptEnabled = true
        view.loadUrl(url)
    }
}
