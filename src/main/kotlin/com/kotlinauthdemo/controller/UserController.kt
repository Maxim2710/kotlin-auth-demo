package com.kotlinauthdemo.controller

import com.kotlinauthdemo.dto.authorization.UserAuthResponseDto
import com.kotlinauthdemo.dto.authorization.UserLoginDto
import com.kotlinauthdemo.dto.deleteuser.DeleteUserResponseMsg
import com.kotlinauthdemo.dto.registration.UserRegistrationDto
import com.kotlinauthdemo.dto.registration.UserResponseDto
import com.kotlinauthdemo.dto.updatepassword.UpdatePasswordResponseMsg
import com.kotlinauthdemo.dto.updatepassword.UserPasswordUpdateDto
import com.kotlinauthdemo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponseDto> {
        val response = userService.getUserById(id)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/update-password")
    fun updatePassword(
        @RequestBody passwordUpdate: UserPasswordUpdateDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<UpdatePasswordResponseMsg> {
        return try {
            userService.updatePassword(passwordUpdate, token)
            ResponseEntity.ok(UpdatePasswordResponseMsg("Password updated successfully"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(UpdatePasswordResponseMsg(e.message ?: "Invalid request"))
        }
    }

    @DeleteMapping("/delete-user")
    fun deleteUser(@RequestHeader("Authorization") token: String): ResponseEntity<DeleteUserResponseMsg> {
        return try {
            userService.deleteUser(token)
            ResponseEntity.ok(DeleteUserResponseMsg("User deleted successfully"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(DeleteUserResponseMsg(e.message ?: "Invalid request"))
        }

    }
}