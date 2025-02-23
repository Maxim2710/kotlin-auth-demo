package com.kotlinauthdemo.dto.registration

data class UserResponseDto(
    val id: Long,
    val username: String,
    val email: String
)