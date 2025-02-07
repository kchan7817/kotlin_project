package com.cookandroid.kotlin_project.backendinterface.group

import com.cookandroid.kotlin_project.backendinterface.dto.GroupDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface group_get {
    @GET("/group/get")
    @Headers("content-type: application/json",
        "accept: application/json")
    fun register(@Header("Authorization") BearerToken : String) : Call<List<GroupDTO>>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "https://seniorsafe.loca.lt/" // 주소

        fun create(): group_get {
            val gson : Gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(group_get::class.java)
        }
    }
}