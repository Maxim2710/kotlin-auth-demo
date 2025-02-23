package com.kotlinauthdemo.model

import jakarta.persistence.*
import org.apache.logging.log4j.util.StringMap

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String
)