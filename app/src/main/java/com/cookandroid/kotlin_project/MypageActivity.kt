package com.cookandroid.kotlin_project

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.cookandroid.kotlin_project.databinding.ActivityMypageBinding
import androidx.appcompat.widget.Toolbar
import com.cookandroid.kotlin_project.backendinterface.dto.UserDTO
import com.cookandroid.kotlin_project.databinding.ActivityJoinBinding
import retrofit2.Call
import retrofit2.Response

class MypageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

     binding = ActivityMypageBinding.inflate(layoutInflater)
     setContentView(binding.root)

     binding.manager.setText("안녕하세요,\t\t${MySharedPreferences.getUserId(this)} 님!")

     binding.myName.setText("이름\t\t\t\t\t\t\t\t\t\t ${MySharedPreferences.getRealName(this)}")

     binding.myBirth.setText("생년월일\t\t\t\t\t\t${MySharedPreferences.getBirthday(this)}")

     binding.myNickname.setText("닉네임\t\t\t\t\t\t\t\t${MySharedPreferences.getUserName(this)}")
        /* //마이페이지 이름 스낵바 클릭 이벤트
         binding.myName.setOnClickListener {
            var snackbar= Snackbar.make(binding.mypagelayout, "${MySharedPreferences.getRealName(this)}", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        //마이페이지 생년월일 스낵바 클릭 이벤트
        binding.myBirth.setOnClickListener {
            var snackbar2= Snackbar.make(binding.mypagelayout, "${MySharedPreferences.getBirthday(this)}", Snackbar.LENGTH_LONG)
            snackbar2.show()
        }
        //마이페이지 닉네임 스낵바 클릭 이벤트
        binding.myNickname.setOnClickListener {
            var snackbar3= Snackbar.make(binding.mypagelayout, "${MySharedPreferences.getUserName(this)}", Snackbar.LENGTH_LONG)
            snackbar3.show()
        }*/

        binding.notice.setOnClickListener {
            var snackbar4= Snackbar.make(binding.mypagelayout,"추후 개발 예정입니다.", Snackbar.LENGTH_LONG)
            snackbar4.show()
        }
        //마이페이지 앱정보 스낵바 클릭 이벤트
        binding.information.setOnClickListener {
            var snackbar5= Snackbar.make(binding.mypagelayout, "MADE BY 용시우, 조경찬, 방대혁, 이경수", Snackbar.LENGTH_LONG)
            snackbar5.show()
        }
    }
}