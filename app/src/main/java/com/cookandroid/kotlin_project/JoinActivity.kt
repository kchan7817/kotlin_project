package com.cookandroid.kotlin_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cookandroid.kotlin_project.backendinterface.auth.signup
import com.cookandroid.kotlin_project.backendinterface.dto.UserDTO
import com.cookandroid.kotlin_project.databinding.ActivityJoinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    val api = signup.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val binding = ActivityJoinBinding.inflate(layoutInflater) // java의 findviewbyid 작업을 안해도됨

        setContentView(binding.root)

        binding.btnCheck.setOnClickListener{
            val data = UserDTO(
                "default_token",
                binding.edtBirthday.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtNickname.text.toString(),
                binding.edtName.text.toString(),
                binding.edtPasswd.text.toString(),
            )
            api.register(data).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    val result = response.code();
                    if(result in 200..299) {
                        Log.d("회원가입성공", response.body().toString())
                        finish()
                    }
                    else {
                        Log.w("회원가입실패", response.body().toString())
                        if (binding.edtPasswd != binding.edtPasswdCheck){
                            Toast.makeText(this@JoinActivity,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
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