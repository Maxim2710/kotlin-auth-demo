package com.kotlinauthdemo.controller

import com.kotlinauthdemo.dto.authorization.UserAuthResponseDto
import com.kotlinauthdemo.dto.authorization.UserLoginDto
import com.kotlinauthdemo.dto.registration.UserRegistrationDto
import com.kotlinauthdemo.dto.registration.UserResponseDto
import com.kotlinauthdemo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody registrationDto: UserRegistrationDto): ResponseEntity<UserResponseDto> {
        val response = userService.registerUser(registrationDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/auth")
    fun authenticateUser(@RequestBody loginDto: UserLoginDto): ResponseEntity<UserAuthResponseDto> {
        val response = userService.authenticateUser(loginDto)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/list-users")
    fun getAllUsers(): ResponseEntity<List<UserResponseDto>> {
        val response = userService.getAllUsers()
        return ResponseEntity.ok(response)
    }
}