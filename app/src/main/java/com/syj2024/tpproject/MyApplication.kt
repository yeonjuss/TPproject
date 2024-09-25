package com.syj2024.tpproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    //  kakao init
      KakaoSdk.init(this,"6e511b1306ea27cacebf67fc93d35c06")

    }
}