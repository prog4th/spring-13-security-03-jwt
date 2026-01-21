package kr.ac.kumoh.s20260000.spring13security03jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.cookies
            ?.find {
                it.name == "accessToken"
            }?.value

        if (token != null && jwtUtil.validateToken(token)) {
            val username = jwtUtil.extractUsername(token)

            val userDetails = User.withUsername(username)
                .password("")
                .roles("USER")
                .build()
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            // 유저 정보를 SecurityContext에 저장
            // @AuthenticationPrincipal 등으로 꺼낼 수 있음
            SecurityContextHolder.getContext()
                .authentication = authentication
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response)
    }
}