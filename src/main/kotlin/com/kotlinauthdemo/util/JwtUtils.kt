package com.kotlinauthdemo.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap

@Component
class JwtUtils {

    private val jwtSecret: String = "somesecretcodehere"

    private val hmacKey: Key = SecretKeySpec(
        Base64.getDecoder().decode(jwtSecret),
        SignatureAlgorithm.HS256.jcaName
    )

    fun generateToken(email: String, id: Long): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["email"] = email
        claims["id"] = id

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, hmacKey)
            .compact()
    }

    fun extractEmailFromJwt(jwt: String): String? {
        val token = jwt.substringAfter(' ')
        val jws : Jws<Claims> = Jwts.parser()
            .setSigningKey(hmacKey)
            .parseClaimsJws(token)

        return jws.body["email"] as? String
    }

    fun extractUserIdFromJwt(jwt: String): Long? {
        val token = jwt.substringAfter(' ')
        val jws: Jws<Claims> = Jwts.parser()
            .setSigningKey(hmacKey)
            .parseClaimsJws(token)

        return (jws.body["id"] as? Number)?.toLong()
    }
}