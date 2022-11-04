package com.cookandroid.kotlin_project

import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.WebSocketListener
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class StompApplication : WebSocketListener() {
    fun main() {

        val logger = Logger.getLogger("Main")

        var stompConnection: Disposable
        var topic: Disposable

        val url = "ws://kangtong1105.codns.com:8080/ws-stomp"
        val sub_url = "/sub/chat/room/AKd1sofsmLm"
        val send_url = "/pub/chat/message"
        val intervalMillis = 1000L
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        val stomp = StompClient(client, intervalMillis).apply { this@apply.url = url }

        // connect
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {

                }
                Event.Type.CLOSED -> {

                }
                Event.Type.ERROR -> {

                }
                else -> {}
            }
        }
        // subscribe
        topic = stomp.join(sub_url)
            .subscribe { logger.log(Level.INFO, it) }

        // send
        stomp.send(send_url ,"nice to meet you").subscribe {
            if (it) {
            }
        }
        //unsubcribe
        topic.dispose()

        // disconnect
        stompConnection.dispose()
    }

}
