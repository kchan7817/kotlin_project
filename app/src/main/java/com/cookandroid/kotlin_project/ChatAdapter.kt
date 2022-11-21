package com.cookandroid.kotlin_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.kotlin_project.databinding.ReceiveMsgBinding
import com.cookandroid.kotlin_project.databinding.SendMsgBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = mutableListOf<User>()

    fun addItem(item: User) {//아이템 추가
        if (data != null) {
            data.add(item)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        //getItemViewType 에서 뷰타입 1을 리턴받았다면 내채팅레이아웃을 받은 Holder를 리턴
        if(viewType == 1){
            return Holder_send(SendMsgBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            return Holder_receive(ReceiveMsgBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (getItemViewType(i) == 1) {
            (viewHolder as Holder_send).bind(data[i])
            viewHolder.setIsRecyclable(false)
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else {
            (viewHolder as Holder_receive).bind(data[i])
            viewHolder.setIsRecyclable(false)
        }
    }
    //내가친 채팅 뷰홀더
    inner class Holder_send(private val binding: SendMsgBinding) : RecyclerView.ViewHolder(binding.root) {
        //친구목록 모델의 변수들 정의하는부분
        fun bind(user: User){
            val dateFormat = SimpleDateFormat("HH:mm")
            val date = Date(System.currentTimeMillis())
            binding.sendMsg.text = user.message
            binding.sendTime.text = dateFormat.format(date).toString()
        }
    }
    //상대가친 채팅 뷰홀더
    inner class Holder_receive(private val binding: ReceiveMsgBinding) : RecyclerView.ViewHolder(binding.root) {
        //친구목록 모델의 변수들 정의하는부분
        fun bind(user: User){
            val dateFormat = SimpleDateFormat("HH:mm")
            val date = Date(System.currentTimeMillis())
            binding.receiveName.text = user.realname
            binding.receiveMsg.text = user.message
            binding.receiveTime.text = dateFormat.format(date).toString()
        }
    }
    /*override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if () {
            1
        } else {
            2
        }
    }*/

}