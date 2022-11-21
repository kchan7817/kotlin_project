package com.cookandroid.kotlin_project

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.kotlin_project.BuildConfig.api_key
import com.cookandroid.kotlin_project.stomp.StompClientService
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.OkHttpClient


class MainActivity_maps : AppCompatActivity(), OnMapReadyCallback{
    val sub_url = "/sub/chat/room/OoMaprWQ7XKEU"
    var msg = "제발 좀 성공 좀 하자 응???"

    var TAG:String = "로그"
    val client = OkHttpClient()
    val intervalMillis = 1000L
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private lateinit var stompService : StompClientService
    private var mBound : Boolean = false;

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            Log.d("Info", "Success to connect Stomp Service")

            val binder = service as StompClientService.LocalBinder
            stompService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

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
                Log.d("DEBUG", "condition")
                return true
            }
            R.id.location ->{
                Log.d("DEBUG", "location")
                return true
            }
            R.id.group ->{
                Log.d("DEBUG", "group")
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

        Intent(this, StompClientService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_EXTERNAL_SERVICE)
        }
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
