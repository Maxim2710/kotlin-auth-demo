package com.kotlinauthdemo.service

import com.kotlinauthdemo.dto.registration.UserRegistrationDto
import com.kotlinauthdemo.dto.registration.UserResponseDto
import com.kotlinauthdemo.model.User
import com.kotlinauthdemo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
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
}