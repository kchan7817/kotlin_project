package com.cookandroid.kotlin_project

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.kotlin_project.databinding.ActivityChatApplicationBinding

class ChatApplication : AppCompatActivity() {

    val mAdapter = ChatAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_application)

        val binding = ActivityChatApplicationBinding.inflate(layoutInflater)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.setHasFixedSize(true)

        /*
        /*binding.messageActivityImageButton.setOnClickListener{
            val user_data = User(
                binding.messageActivityEditText.text.toString(),
            )
        }*/
        */
    }
}