package kr.ac.kumoh.s20260000.spring13security03jwt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
// TODO: 실행 결과 확인 후에는 주석 처리할 것
@EnableWebSecurity(debug = true)
class SecurityConfig(private val jwtUtil: JwtUtil) {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService() = InMemoryUserDetailsManager()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            httpBasic { disable() }
            formLogin { disable() }
            logout { disable() }

            authorizeHttpRequests {
                listOf(
                    "/",
                    "/signup",
                    "/signup.html",
                    "/login",
                    "/login.html",
                    "/logout"
                ).forEach {
                    authorize(it, permitAll)
                }
//                authorize("/", permitAll)
//                authorize("/signup", permitAll)
//                authorize("/signup.html", permitAll)
//                authorize("/login", permitAll)
//                authorize("/login.html", permitAll)
//                authorize("/logout", permitAll)

                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(
                JwtAuthenticationFilter(jwtUtil)
            )
        }
        return http.build()
    }
}