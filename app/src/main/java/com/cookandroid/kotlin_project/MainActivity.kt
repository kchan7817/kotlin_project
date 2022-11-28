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
import com.cookandroid.kotlin_project.stomp.StompClientService
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

        // SharedPreferences 안에 값이 저장되어 있지 않을 때 -> Login
        if(MySharedPreferences.getUserId(this).isNullOrBlank()
            || MySharedPreferences.getUserPass(this).isNullOrBlank()) {
            api_singin
        }
        else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity_maps로 이동
            Toast.makeText(this, "${MySharedPreferences.getUserId(this)}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, MainActivity_maps::class.java)
            startActivity(intent2)
            finish()
        }


        val intent: Intent = Intent(this, JoinActivity::class.java)//intent 선언
        val intent2: Intent = Intent(this, MainActivity_maps::class.java)
        val intent3: Intent = Intent(this, StompClientService::class.java)
        val binding = ActivityMainBinding.inflate(layoutInflater)// java의 findviewbyid 작업을 안해도됨


        setContentView(binding.root)

        binding.btnJoin.setOnClickListener {
            startActivity(intent)
        }
        // 자동로그인버튼
        binding.rgLoginKeep.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            //스위치가 켜지면
            if(onSwitch) {
                MySharedPreferences.setUserId(this, binding.email.text.toString())
                MySharedPreferences.setUserPass(this, binding.PW.text.toString())
                Toast.makeText(this, "자동로그인 ON", Toast.LENGTH_SHORT).show()
            }
            //스위치가 꺼지면
            else{
                MySharedPreferences.clearUser(this)
                Toast.makeText(this, "자동로그인 OFF", Toast.LENGTH_SHORT).show()
            }
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
                        MySharedPreferences.setUserId(this@MainActivity, binding.email.text.toString())
                        MySharedPreferences.setUserPass(this@MainActivity, binding.PW.text.toString())
                        Log.d("로그인성공", response.body().toString())
                        intent3.putExtra("token_login", response.body()!!.token)
                        startService(intent3)
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