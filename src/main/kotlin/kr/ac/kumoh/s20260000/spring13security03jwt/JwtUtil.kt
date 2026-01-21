package kr.ac.kumoh.s20260000.spring13security03jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
    // TODO: 지난 강의 참고하여 환경 변수 사용할 것
    private val secretKey = "a-string-secret-at-least-256-bits-long"

    private val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    private val expirationTime = 20L * 1000 // 20초

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            claims.payload.expiration.after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload.subject
    }
}