package com.syj2024.tpproject.activities

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.syj2024.tpproject.databinding.ActivitySingupBinding

class SignupActivity : AppCompatActivity() {

    val binding by lazy { ActivitySingupBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 클릭 반응
        binding.toolbar.setNavigationOnClickListener { finish() }

        // 가입하기 버튼 클릭 반응
        binding.btnSignup.setOnClickListener { clickSingup() }
    }

    private fun clickSingup() {
        //Firebase Firestore DB에 사용자 정보 저장하기

        var email=binding.etEmail.text.toString()
        var password=binding.etPassword.text.toString()
        var passwordConfirm=binding.etPasswordConfirm.text.toString()

        // 패스워드와 패스워드 확인의 글씨가 같은지 검사
        if(password!=passwordConfirm) {
            AlertDialog.Builder(this).setMessage("패스워드 확인에 문제가 있습니다. 다시 확인하여 입력해주세요").create().show()
            binding.etPasswordConfirm.selectAll()
            return

        }

        // Firestore db에 저장하기 - "account" 컬렉선에 회원의 계정 정보 저장
        // 중복된 이메일 정보가 있는지 확인 후 저장
        val accountRef: CollectionReference = Firebase.firestore.collection("account")
        accountRef.whereEqualTo("email",email).get().addOnSuccessListener {
            // 같은 값을 가진 Document가  존재한다면 같은 이메일이 존재하는 것임
            if (it.documents.size > 0) {
                AlertDialog.Builder(this).setMessage("중복된 이메일이 있습니다. 다시 입력해주세요").create().show()
                binding.etEmail.selectAll()

            } else {
                val user:MutableMap<String,String> = mutableMapOf()
                user.put("email", email)
                user["password"]= password
                user["type"]="email"

                accountRef.document().set(user).addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setMessage("축하합니다.\n 회원가입이 완료되었습니다.")
                        .setPositiveButton("확인",object : OnClickListener{
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                finish()
                            }


                        }).create().show()


                }.addOnFailureListener {

                    Toast.makeText(this, "회원가입 오류 발생. 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

}