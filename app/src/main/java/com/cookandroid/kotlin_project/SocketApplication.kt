import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.Socket
import java.net.URISyntaxException


class SocketApplication : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("성공이다 이거야~^^")
        Log.d("greenfrog","로그창 성공")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        output("받아랏!!: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSSURE_STATUS, null)
        output("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        output("ERROR: " + t.message)
    }

    fun output(text: String) {
        Log.d("websocket", text!!)

    }

    companion object {
        private const val NORMAL_CLOSSURE_STATUS = 1000
    }
}
