package com.cookandroid.kotlin_project

import java.util.regex.Pattern

class Message {

    var command: String? = null
    var payload: String? = null
    var headers: Map<String, String> = HashMap()

    constructor(command: String, headers: Map<String, String>) {
        this.command = command
        this.headers = headers
    }

    constructor(command: String, headers: Map<String, String>, payload: String) {
        this.command = command
        this.headers = headers
        this.payload = payload
    }

    constructor(command: String, payload: String) {
        this.command = command
        this.payload = payload
    }

    constructor(command: String) {
        this.command = command
    }

    companion object {

        val TERMINATE_MESSAGE_SYMBOL = "\u0000"
        val PATTERN_HEADER = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)")
    }
}