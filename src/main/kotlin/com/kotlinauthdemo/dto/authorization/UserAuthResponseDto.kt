package com.kotlinauthdemo.dto.authorization

data class UserAuthResponseDto(
    val id: Long,
    val username: String,
    val email: String,
    val token: String
)
