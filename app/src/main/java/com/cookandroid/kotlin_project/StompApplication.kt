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

                    // subscribe
                    topic = stomp.join(url)
                        .subscribe { logger.log(Level.INFO, it) }

                    Log.d("제에에에발~~~","좀 되라 제발..")

                    // unsubscribe
                    topic.dispose()

                    // send
                    stomp.send(url , "hihi").subscribe {
                        if (it) {
                        }
                    }
                }
                Event.Type.CLOSED -> {
                    Log.d("메롱~","닫혔지롱~")
                }
                Event.Type.ERROR -> {
                    Log.e("아 씨발","또안되네;")
                }
                else -> {}
            }
        }


        // disconnect
        stompConnection.dispose()
    }

}
