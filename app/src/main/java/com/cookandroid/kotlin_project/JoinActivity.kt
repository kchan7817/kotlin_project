package com.cookandroid.kotlin_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cookandroid.kotlin_project.databinding.ActivityJoinBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class JoinActivity : AppCompatActivity() {

    val api = signservice.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val binding = ActivityJoinBinding.inflate(layoutInflater) // java의 findviewbyid 작업을 안해도됨

        setContentView(binding.root)

        /*
        var realname = binding.edtName.text.toString()
        var birthday  = binding.edtBirthday.text.toString()
        var username = binding.edtJoinId.text.toString()
        var password = binding.edtPasswd.text.toString()
        var email = binding.edtEmail.text.toString()
        Log.d("api value test", "aaaa" + realname)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://kangtong1105.codns.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(signservice::class.java)
        val userInfo: Call<LoginResponse> = service.register(LoginResponse(birthday="$birthday", email="$email", username="$username", realname="$realname", password = "$password"))
        */

        binding.btnCheck.setOnClickListener{
            val data = LoginResponse(
                binding.edtBirthday.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtNickname.text.toString(),
                binding.edtName.text.toString(),
                binding.edtPasswd.text.toString(),

                )
            api.register(data).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val result = response.code();
                    if(result in 200..299)
                        Log.d("회원가입성공", response.body().toString())
                    else {
                        Log.w("회원가입실패", response.body().toString())
                        if (binding.edtPasswd != binding.edtPasswdCheck){
                            Toast.makeText(this@JoinActivity,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("연결 실패","${t.localizedMessage}")
                }
            })


            /*var builder = AlertDialog.Builder(this)
            builder.setTitle("이메일 인증")
            var v1 = layoutInflater.inflate(R.layout.activity_dialog_custom, null)
            builder.setView(v1)
            builder.setPositiveButton("확인",null)
            builder.setNegativeButton("취소",null)
            builder.show()*/

        }

    }
}

interface signservice{
    @POST("/auth/signup")
    @Headers("content-type: application/json",
        "accept: application/json")
    fun register(@Body jsonparams: LoginResponse) : Call<LoginResponse>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://121.172.14.80:8080" // 주소

        fun create(): signservice {

            val gson : Gson = GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(signservice::class.java)
        }
    }
}