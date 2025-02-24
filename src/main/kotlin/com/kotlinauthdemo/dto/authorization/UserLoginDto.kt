package com.kotlinauthdemo.dto.authorization

data class UserLoginDto(
    val email: String,
    val password: String
)