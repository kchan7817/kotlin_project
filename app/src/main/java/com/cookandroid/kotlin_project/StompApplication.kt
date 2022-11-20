package com.cookandroid.kotlin_project

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
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
        val send_url = "/pub/chat/message"
        lateinit var topic: Disposable
        lateinit var stompConnection: Disposable
        var sub_url = "/sub/chat/room/OoMaprWQ7XKEU"
        val intervalMillis = 1000L
        val client = OkHttpClient()
        var msg = "제발 좀 성공 좀 하자 응???"

        val stomp = StompClient(client, intervalMillis)
        //topic = stomp.join(sub_url).subscribe { logger.log(Level.INFO, it) }

        /*fun subscribe(sub_url: String) {
            topic = stomp.join(sub_url)
                .subscribe { logger.log(Level.INFO, it) }
        }

        fun send(msg: String) {
            stomp.send(send_url, msg).subscribe {
                if (it) {

                }
            }
        }

        fun unsubscribe() {
            topic.dispose()
        }
        /*fun disconnect(){
            stompConnection.dispose()
        }*/

        // connect
        fun stompconnect() {
            stomp.connect().subscribe {
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
        }

        fun disconnect() {
            stompConnection.dispose()
        }*/

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
        stomp.send(send_url, msg).subscribe {
            if (it) {

            }
        }

        //unsubcribe
        topic.dispose()

        // disconnect
        stompConnection.dispose()
    }

}
