package com.team6.onandthefarm.config;


import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.security.CustomAccessDeniedHandler;
import com.team6.onandthefarm.security.CustomAuthenticationEntryPoint;
import com.team6.onandthefarm.security.jwt.JwtAuthenticationFilter;
import com.team6.onandthefarm.security.jwt.JwtExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserRepository userRepository, JwtAuthenticationFilter jwtAuthenticationFilter, JwtExceptionFilter jwtExceptionFilter, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        this.userRepository = userRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtExceptionFilter = jwtExceptionFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // csrf 미적용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 하지않음
                .and()
                .httpBasic().authenticationEntryPoint(customAuthenticationEntryPoint)   // 인증 되지 않은 유저가 요청했을때 동작
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler) // 액세스 할 수 없는 요청 했을 시 동작
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/login", "/api/seller/login", "/api/seller/signup").permitAll()
                .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/seller/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and().cors();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // controller 시작 전에 jwt 인증을 하기 위한 필터 등록
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class); // jwt 인증 시 발생하는 exception 처리 필터 등록

    }
}