package com.syj2024.tpproject.activities

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.syj2024.tpproject.R
import com.syj2024.tpproject.databinding.ActivityPlaceUrlBinding

class PlaceUrlActivity : AppCompatActivity() {

    private  val binding by lazy { ActivityPlaceUrlBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.wv.webViewClient= WebViewClient()  // 현재 웹뷰안에서 웹문서가 열리도록 ..
        binding.wv.webChromeClient= WebChromeClient() // 웹문서 안에서 다이얼로그 같은 것들이 발동하도록..

        binding.wv.settings.javaScriptEnabled= true // 웹뷰는 기본적으로 보안문제로 js 동작을 막아놓았기에 실행 되도록 ..

        val place_url:String= intent.getStringExtra("place_url") ?: ""
        binding.wv.loadUrl(place_url)



    } //onCreate method

    // 디바이스의 뒤로가기 버튼이 클릭되었을때..
    override fun onBackPressed() {
        // 웹 뷰의 뒤로갈 페이지가 존재하는지 확인..
        if (binding.wv.canGoBack()) binding.wv.goBack()
        else super.onBackPressed()
    }

}