package com.cookandroid.kotlin_project.backendinterface.dto

data class MemberDTO (
    val id: String? = null,
    val nickname: String? = null,
    val isManager: Boolean? = null,
    val group: GroupDTO? = null,
    val stompToken: String? = null,
)