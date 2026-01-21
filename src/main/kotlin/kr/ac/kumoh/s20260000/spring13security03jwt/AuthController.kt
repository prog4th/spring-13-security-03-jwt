package kr.ac.kumoh.s20260000.spring13security03jwt

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userDetailsService: InMemoryUserDetailsManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/signup")
    fun signup(
        @RequestParam username: String,
        @RequestParam password: String
    ): String {
        if (userDetailsService.userExists(username))
            return "이미 존재함"

        val newUser = User.withUsername(username)
            .password(passwordEncoder.encode(password))
            .roles("USER").build()
        userDetailsService.createUser(newUser)
        return "가입 성공! <a href='/login.html'>로그인</a>"
    }

    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
        response: HttpServletResponse
    ): String {
        val userDetails = try {
            userDetailsService.loadUserByUsername(username)
        } catch (e: Exception) {
            null
        }

        if (userDetails != null &&
            passwordEncoder.matches(
                password,
                userDetails.password
            )) {
            val token = jwtUtil.generateToken(username)
            val cookie = Cookie(
                "accessToken",
                token).apply {
                isHttpOnly = true
                path = "/"
                maxAge = 20 // 20초
            }
            response.addCookie(cookie)

            return "로그인 성공! <a href='/hello'>들어가기</a>"
        }
        return "로그인 실패"
    }

    @GetMapping("/logout")
    fun logout(
        response: HttpServletResponse
    ): String {
        val cookie = Cookie(
            "accessToken",
            null).apply {
            path = "/"
            maxAge = 0
        }
        response.addCookie(cookie)

        return "로그아웃 성공"
    }
}