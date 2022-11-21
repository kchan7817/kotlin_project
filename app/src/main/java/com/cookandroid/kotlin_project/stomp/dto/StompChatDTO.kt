package com.cookandroid.kotlin_project.stomp.dto

import com.cookandroid.kotlin_project.backendinterface.dto.MemberDTO


class StompChatDTO(
    val sender: MemberDTO ?= null,
    val payload: String ?= null,
    val sendTime: String ?= null,
)
