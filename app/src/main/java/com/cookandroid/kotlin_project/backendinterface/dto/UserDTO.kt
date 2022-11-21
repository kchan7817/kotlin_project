package com.cookandroid.kotlin_project.backendinterface.dto

data class UserDTO(
    val token: String = "",
    val birthday: String = "",
    val email: String = "",
    val username: String = "",
    val realname: String = "",
    val password: String = ""
)