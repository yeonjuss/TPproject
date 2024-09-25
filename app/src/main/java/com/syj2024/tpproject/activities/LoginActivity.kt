package com.syj2024.tpproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.syj2024.tpproject.G
import com.syj2024.tpproject.R
import com.syj2024.tpproject.data.NidUserInfoResponse
import com.syj2024.tpproject.data.UserAccout
import com.syj2024.tpproject.databinding.ActivityLoginBinding
import com.syj2024.tpproject.network.RetrofitHelper
import com.syj2024.tpproject.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 둘러보기 글씨 클릭으로 로그인 없이 Main 화면으로 이동
        binding.tvGo.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        // 회원가입 버튼 클릭
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))

        }
        // 이메일 로그인 버튼
        binding.btnLoginEmail.setOnClickListener {
            startActivity(Intent(this,EmailLoginActivity::class.java))
        }

        // 로그인 api 연동 버튼 처리
        binding.btnLoginKakao.setOnClickListener { clickKakao() }
        binding.btnLoginGoogle.setOnClickListener { clickGoogle() }
        binding.btnLoginNaver.setOnClickListener { clickNaver() }


        // kakao에서 사용하는 keyHash 인증서 지문 값 얻어오기
        val keyHash:String= Utility.getKeyHash(this)
        Log.i("keyHash",keyHash)


    } // onCreate method..
    private fun clickKakao() {
        // kakao login api library 추가하기

        // 카카오 로그인 공통 callback 구성
        val callback:(OAuthToken?, Throwable?)->Unit= { token, error ->
            if (error != null) {
                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()

                // 사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if(user !=null){
                        val id:String= user.id.toString()
                        val email:String= user.kakaoAccount?.email ?:""
                        val nickname:String=user.kakaoAccount?.profile?.nickname ?:""
                        val profileImg:String?=user.kakaoAccount?.profile?.profileImageUrl

                        Toast.makeText(this, "$email \n $nickname", Toast.LENGTH_SHORT).show()
                        G.userAccount= UserAccout(id,email,"kakako")

                        // main화면으로 이동
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()

                    }

                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인 , 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }

    }

    private fun clickGoogle() {

    }

    private fun clickNaver() {

        // 1. 네이버 개발자 사이트에 앱 등록 및 라이브러리 추가
        // 2. 네아로 sdk 초기화
        NaverIdLoginSDK.initialize(this,"rsbZkuQ5rP5sEemeAYgx","3HvxeIjrTt","where")

        //3. 로그인 작업 수행
        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                Toast.makeText(this@LoginActivity, "error: $message", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(this@LoginActivity, "failure :$message", Toast.LENGTH_SHORT).show()

            }

            override fun onSuccess() {
                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                // 회원 프로필정보는 REST API를 이용하여 요청하고 응답받아야 함 - API 사용명세서 확인

                // 회원 정보를 얻어오기 위한 통행증
                val accessToken:String?= NaverIdLoginSDK.getAccessToken()

                // 토큰번호가 잘 오는지 확인
                Log.i("token","$accessToken")

                // Retrofit 작업을 통해 사용자 정보 가져오기
                val retrofit= RetrofitHelper.getRetrofitInstance("https://openapi.naver.com")
                val retrofitService= retrofit.create(RetrofitService::class.java)
                val call= retrofitService.getNidUserInfo("Bearer $accessToken")
                call.enqueue(object : Callback<NidUserInfoResponse>{
                    override fun onResponse(
                        p0: Call<NidUserInfoResponse>,
                        p1: Response<NidUserInfoResponse>
                    ) {
                        // 응답 객체 p1으로 부터.. 결과로 받은 회원정보 json데이터를 NidUserInfoResponse 로 분석한 결과 받기
                        val userInfo= p1.body()
                        val id:String= userInfo?.response?.id ?: ""
                        val email:String= userInfo?.response?.email ?:""

                        Toast.makeText(this@LoginActivity, "이메일정보: $email", Toast.LENGTH_SHORT).show()
                        G.userAccount=UserAccout(id,email,"naver")

                        // 로그인 성공했으니 .. main화면으로 이동
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        finish()
                    }

                    override fun onFailure(p0: Call<NidUserInfoResponse>, p1: Throwable) {
                        Toast.makeText(this@LoginActivity, "회원정보 불러오기 실패: ${p1.message}", Toast.LENGTH_SHORT).show()
                    }


                })


            }
        })
    }




}



