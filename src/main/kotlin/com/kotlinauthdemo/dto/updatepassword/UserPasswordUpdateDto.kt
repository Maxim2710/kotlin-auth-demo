package com.kotlinauthdemo.dto.updatepassword

data class UserPasswordUpdateDto(
    val oldPassword: String,
    val newPassword: String
)
