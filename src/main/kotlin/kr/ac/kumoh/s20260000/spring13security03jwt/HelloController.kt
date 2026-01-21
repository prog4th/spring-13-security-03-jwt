package kr.ac.kumoh.s20260000.spring13security03jwt

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello(authentication: Authentication): String {
        // (방법 1) 이렇게 꺼내는 것이 편하고 일반적임
        val username = authentication.name

        // (방법 2) 이렇게 principal을 통해서 꺼내도 되긴 함
//        val userDetails = authentication.principal as UserDetails
//        val username = userDetails.username

        return "$username 님, 안녕하세요.  <a href='/logout'>로그아웃</a>"
    }
}