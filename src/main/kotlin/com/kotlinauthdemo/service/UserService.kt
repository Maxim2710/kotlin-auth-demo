package com.kotlinauthdemo.service

import com.kotlinauthdemo.dto.authorization.UserAuthResponseDto
import com.kotlinauthdemo.dto.authorization.UserLoginDto
import com.kotlinauthdemo.dto.registration.UserRegistrationDto
import com.kotlinauthdemo.dto.registration.UserResponseDto
import com.kotlinauthdemo.dto.updatepassword.UserPasswordUpdateDto
import com.kotlinauthdemo.model.User
import com.kotlinauthdemo.repository.UserRepository
import com.kotlinauthdemo.util.JwtUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {

    fun registerUser(registrationDto: UserRegistrationDto): UserResponseDto {
        if (userRepository.findByUsername(registrationDto.username) != null) {
            throw IllegalArgumentException("Username is already taken")
        }

        if (userRepository.findByEmail(registrationDto.email) != null) {
            throw IllegalArgumentException("Email is already registered")
        }

        val hashedPassword = passwordEncoder.encode(registrationDto.password)
        val newUser = User(
            username = registrationDto.username,
            email = registrationDto.email,
            password = hashedPassword
        )

        val savedUser = userRepository.save(newUser)

        return UserResponseDto(
            id = savedUser.id,
            username = savedUser.username,
            email = savedUser.email
        )
    }

    fun authenticateUser(loginDto: UserLoginDto): UserAuthResponseDto {
        val user = userRepository.findByEmail(loginDto.email)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!passwordEncoder.matches(loginDto.password, user.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }

        val token = jwtUtils.generateToken(
            user.email,
            user.id
        )

        return UserAuthResponseDto(
            id = user.id,
            username = user.username,
            email = user.email,
            token = token
        )
    }

    fun getAllUsers(): List<UserResponseDto> {
        return userRepository.findAll().map { user ->
            UserResponseDto(
                id = user.id,
                username = user.username,
                email = user.email
            )
        }
    }

    fun getUserById(id: Long): UserResponseDto {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("User not found with id: $id") }

        return UserResponseDto(
            id = user.id,
            username = user.username,
            email = user.email
        )
    }

    fun updatePassword(passwordUpdate: UserPasswordUpdateDto, token: String) {
        val userId = jwtUtils.extractUserIdFromJwt(token) ?: throw IllegalArgumentException("Invalid token")

        val user = userRepository.findById(userId)
            .orElseThrow { throw IllegalArgumentException("User not found with id: $userId")}

        if (!passwordEncoder.matches(passwordUpdate.oldPassword, user.password)) {
            throw IllegalArgumentException("Old password is incorrect")
        }

        if (passwordEncoder.matches(passwordUpdate.newPassword, user.password)) {
            throw IllegalArgumentException("New password must be different from the old one")
        }

        user.password = passwordEncoder.encode(passwordUpdate.newPassword)

        userRepository.save(user)
    }

    fun deleteUser(token: String) {
        val userId = jwtUtils.extractUserIdFromJwt(token) ?: throw IllegalArgumentException("Invalid token")

        val user = userRepository.findById(userId)
            .orElseThrow { throw IllegalArgumentException("User not found with id: $userId") }

        userRepository.delete(user)
    }


}