package kr.ac.kumoh.s20260000.spring13security03jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello(authentication: Authentication): String {
        val username = authentication.name

        // 이렇게 principal을 통해서 꺼내도 되긴 함
//        val userDetails = authentication.principal as UserDetails
//        val username = userDetails.username

        return "$username 님, 안녕하세요.  <a href='/logout'>로그아웃</a>"
    }

    // @AuthenticationPrincipal 사용 (권장)
    @GetMapping("/user")
    fun getUser(
        @AuthenticationPrincipal user: UserDetails
    ): String {
        return "username: ${user.username}  <a href='/logout'>로그아웃</a>"
    }
}