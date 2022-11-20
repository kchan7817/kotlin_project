package com.cookandroid.kotlin_project

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.*
import okio.ByteString
import java.io.StringReader
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger

class StompClient(private val okHttpClient: OkHttpClient,
                  private val reconnectAfter: Long) :
    WebSocketListener() {

    private val logger = Logger.getLogger(javaClass.name)

    private val DEFAULT_ACK = "auto"
    private val SUPPORTED_VERSIONS = "1.1,1.2"

    private val topics = HashMap<String, String>()
    private val emitters = ConcurrentHashMap<String, ObservableEmitter<String>>()

    private var shouldBeConnected: Boolean = false
    private var connected = false

    private lateinit var webSocket: WebSocket

    private lateinit var emitter: ObservableEmitter<Event>

    private val url: String = "ws://kangtong1105.codns.com:8080/ws-stomp"
    private val token = "eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJLZXkiOiJ4ZmdkcFRmbEdMIiwiZ3JvdXBJZCI6IjQwMjhiODgxODQ2MTBmZjIwMTg0NjExMjFmMmMwMDAyIiwiZ3JvdXBLZXkiOiJPb01hcHJXUTdYS0VVIiwibWVtYmVySWQiOiI0MDI4Yjg4MTg0NjEwZmYyMDE4NDYxMTIxZjQ0MDAwMyIsImlzcyI6ImRlbW8gYXBwIiwiaWF0IjoxNjY4MDc1ODk1LCJleHAiOjE2NjgxNjIyOTV9.7LIycl2L4BXQnuBf151YnzOcfobpXP4u2xNQegBI4z94XoBE3__bLgHQT6mnZfnr4ltzpzmMnCAgWKHoZ6-3fg"

    fun connect(): Observable<Event> {
        return Observable
            .create<Event> {
                emitter = it
                shouldBeConnected = true
                open()
            }
            .doOnDispose {
                close()
                shouldBeConnected = false
            }
    }

    fun join(topic: String): Observable<String> {
        return Observable
            .create<String> {

                val topicId = UUID.randomUUID().toString()
                val headers = HashMap<String, String>()
                headers[Headers.ID] = topicId
                headers[Headers.DESTINATION] = topic
                headers[Headers.ACK] = DEFAULT_ACK
                headers.put("token",token)
                webSocket.send(compileMessage(Message(Commands.SUBSCRIBE, headers)))

                emitters[topic] = it
                topics[topic] = topicId

                logger.log(Level.INFO, "Subscribed to: $topic id: $topicId")

            }
            .doOnDispose {

                val topicId = topics[topic]

                val headers = HashMap<String, String>()
                headers[Headers.ID] = topicId!!
                webSocket.send(compileMessage(Message(Commands.UNSUBSCRIBE, headers)))

                emitters.remove(topic)
                topics.remove(topicId)

                logger.log(Level.INFO, "Unsubscribed from: $topic id: $topicId")

            }
    }

    fun send(topic: String, msg: String): Observable<Boolean> {
        return Observable
            .create<Boolean> {
                val headers = HashMap<String, String>()
                headers.put("token",token)
                headers[Headers.DESTINATION] = topic
                val msg_1 = ("{\"payload\"" + ":" + "\"" + msg + "\""+"}")
                it.onNext(webSocket.send(compileMessage(Message(Commands.SEND, headers, msg_1))))
                it.onComplete()
            }
    }

    private fun open() {
        if (!connected) {
            val headers = HashMap<String, String>()
            headers.put("token",token)
            logger.log(Level.INFO, "Connecting...")
            val request = Request.Builder()
                .url(url)
                .build()
            webSocket = okHttpClient.newWebSocket(request, this)
            webSocket.send(compileMessage(Message(Commands.CONNECT,headers)))
            connected = true
        } else {
            logger.log(Level.INFO, "Already connected")
        }
    }

    private fun reconnect() {
        if (shouldBeConnected) {
            close()
            Thread.sleep(reconnectAfter)
            open()
        }
    }

    private fun close() {
        if (connected) {
            logger.log(Level.INFO, "Disconnecting...")
            webSocket.close(Codes.DEFAULT, "")
            connected = false
        } else {
            logger.log(Level.INFO, "Already disconnected")
        }
    }

    private fun parseMessage(data: String?): Message {

        if (data.isNullOrBlank())
            return Message(Commands.UNKNOWN)

        val reader = Scanner(StringReader(data))
        reader.useDelimiter("\\n")
        val command = reader.next()
        val headers = HashMap<String, String>()

        while (reader.hasNext(Message.PATTERN_HEADER)) {
            val matcher = Message.PATTERN_HEADER.matcher(reader.next())
            matcher.find()
            headers.put(matcher.group(1), matcher.group(2))
        }

        reader.skip("\\s")

        reader.useDelimiter(Message.TERMINATE_MESSAGE_SYMBOL)
        val payload = if (reader.hasNext()) reader.next() else null

        return Message(command, headers, payload!!)
    }

    private fun compileMessage(message: Message): String {
        val builder = StringBuilder()

        if (message.command != null)
            builder.append(message.command).append('\n')

        for ((key, value) in message.headers)
            builder.append(key).append(':').append(value).append('\n')
        builder.append('\n')

        if (message.payload != null)
            builder.append(message.payload).append("\n\n")

        builder.append(Message.TERMINATE_MESSAGE_SYMBOL)

        return builder.toString()
    }

    // from WebSocketListener listener

    override fun onOpen(socket: WebSocket, response: Response) {
        val headers = HashMap<String, String>()
        headers[Headers.VERSION] = SUPPORTED_VERSIONS
        webSocket.send(compileMessage(Message(Commands.CONNECT, headers)))
        logger.log(Level.INFO, "onOpen")
    }

    override fun onClosed(socket: WebSocket, code: Int, reason: String) {
        emitter.onNext(Event(Event.Type.CLOSED))
        logger.log(Level.INFO, "onClosed reason: $reason, code: $code")
        reconnect()
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        handleMessage(parseMessage(bytes.toString()))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        handleMessage(parseMessage(text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(code, reason)
        logger.log(Level.INFO, "onClosing reason: $reason, code: $code")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        emitter.onNext(Event(Event.Type.ERROR, t))
        logger.log(Level.INFO, "onFailure", t)
        reconnect()
    }

    private fun handleMessage(message: Message) {
        when (message.command) {
            Commands.CONNECTED -> {
                emitter.onNext(Event(Event.Type.OPENED))
            }
            Commands.MESSAGE -> {
                val dest = message.headers[Headers.DESTINATION]
                if (dest != null) {
                    val emitter = emitters[dest]
                    if (emitter != null) {
                        emitter.onNext(message.payload!!)
                    }
                }
            }
        }
        logger.log(Level.INFO, "onMessage payload: ${message.payload}, heaaders:${message.headers}, command: ${message.command}")
    }


}