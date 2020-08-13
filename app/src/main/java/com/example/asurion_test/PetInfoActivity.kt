package com.example.asurion_test

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class PetInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_info)

        val url=intent.getStringExtra("URL")

        val view: WebView = findViewById(R.id.webView) as WebView
        view.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url)
                return false
            }
        })
        view.getSettings().setJavaScriptEnabled(true)
        view.loadUrl(url)
    }
}
