package de.enricoprojects.movieflex.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
               .cors(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .httpBasic(AbstractHttpConfigurer::disable)
               .formLogin(AbstractHttpConfigurer::disable)
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/error").permitAll()
                       //DataBase
                       .requestMatchers("/h2-console/**").permitAll()
                       //Public Movie GET endpoints
                       .requestMatchers(HttpMethod.GET,"/api/movies").permitAll()
                       .requestMatchers(HttpMethod.GET,"/api/movies/**").permitAll()
                       .requestMatchers(HttpMethod.POST,"/api/movies/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.POST, "/api/movies").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/movies/{movieId}").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasRole("ADMIN")
                        //Public Movie Auth endpoints
                       .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                       .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()
                       .requestMatchers(HttpMethod.POST,"/api/auth/refresh").permitAll()
                       .requestMatchers(HttpMethod.POST,"/api/auth/logout").permitAll()
                       .requestMatchers(HttpMethod.PUT,"/api/movie/*/rating").authenticated()

                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                        //Everything else needs authentification
                        .anyRequest().authenticated()
               ).headers(headers -> headers
                       .frameOptions(frame -> frame.sameOrigin())
               )
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .build();

    }


}
