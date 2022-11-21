package com.cookandroid.kotlin_project

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cookandroid.kotlin_project.backendinterface.auth.signin
import com.cookandroid.kotlin_project.backendinterface.dto.UserDTO
import com.cookandroid.kotlin_project.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    val api_singin = signin.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent: Intent = Intent(this, JoinActivity::class.java)//intent 선언
        val intent2: Intent = Intent(this, MainActivity_maps::class.java)
        val binding = ActivityMainBinding.inflate(layoutInflater)// java의 findviewbyid 작업을 안해도됨

        setContentView(binding.root)

        binding.btnJoin.setOnClickListener{
            startActivity(intent) // binding 쓰는법
        }
        binding.btnLogin.setOnClickListener{
            val data_signin = UserDTO(
                email = binding.email.text.toString(),
                password = binding.PW.text.toString(),
            )
            var dialog = AlertDialog.Builder(this@MainActivity)
            api_singin.register_signin(data_signin).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    val result = response.code();
                    if(result in 200..299) {
                        Log.d("로그인성공", response.body().toString())
                        startActivity(intent2)
                    }
                    else {
                        Log.w("로그인실패", response.body().toString())
                        dialog.setTitle("에러")
                        dialog.setMessage("로그인에 실패하셨습니다.")
                        dialog.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dlg, id -> "확인" })
                        dialog.show()
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Log.e("연결 실패","${t.localizedMessage}")
                    dialog.setTitle("에러")
                    dialog.setMessage("로그인에 실패하셨습니다.")
                    dialog.show()

                }
            })

        }

    }
}