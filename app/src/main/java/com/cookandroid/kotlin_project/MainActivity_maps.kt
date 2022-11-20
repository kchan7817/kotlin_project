package com.cookandroid.kotlin_project

import SocketApplication
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.cookandroid.kotlin_project.BuildConfig.api_key
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.Socket
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.thread



class MainActivity_maps : AppCompatActivity(), OnMapReadyCallback{
    val sub_url = "/sub/chat/room/OoMaprWQ7XKEU"
    var msg = "제발 좀 성공 좀 하자 응???"

    var TAG:String = "로그"
    val client = OkHttpClient()
    val intervalMillis = 1000L
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_maps)


        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(api_key)

        // 뷰 역할을 하는 프래그먼트 객체 얻기
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        // 인터페이스 역할을 하는 NaverMap 객체 얻기
        // 프래그먼트(MapFragment)의 getMapAsync() 메서드로 OnMapReadyCallback 을 등록하면 비동기로 NaverMap 객체를 얻을 수 있다고 한다.
        // NaverMap 객체가 준비되면 OnMapReady() 콜백 메서드 호출
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_option,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.condition ->{
                return true
            }
            R.id.location ->{

                return true
            }
            R.id.group ->{

                return true
            }
            else -> return false
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        Log.d(TAG, "MainActivity - onMapReady")
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.d(TAG, "MainActivity - onRequestPermissionsResult")
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 거부됨")
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 승인됨")
                naverMap.locationTrackingMode = LocationTrackingMode.Follow // 현위치 버튼 컨트롤 활성
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val API_KEY = BuildConfig.API_KEY
    }

}

/*class StompApp(){
        val logger = Logger.getLogger("Main")
        val send_url = "/pub/chat/message"
        lateinit var topic: Disposable
        lateinit var stompConnection: Disposable
        var sub_url = "/sub/chat/room/OoMaprWQ7XKEU"
        val intervalMillis = 1000L
        val client = OkHttpClient()
        var msg = "제발 좀 성공 좀 하자 응???"

        val stomp = StompClient(client, intervalMillis)
        topic = stomp.join(sub_url).subscribe { logger.log(Level.INFO, it) }

        fun subscribe(sub_url: String) {
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
        }

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
        /*topic = stomp.join(sub_url)
            .subscribe { logger.log(Level.INFO, it) }
        // send
        stomp.send(send_url, msg).subscribe {
            if (it) {

            }
        }*/

        //unsubcribe
        //topic.dispose()

        // disconnect
        //stompConnection.dispose()
}*/
