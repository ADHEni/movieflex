package de.enricoprojects.movieflex.security;

import de.enricoprojects.movieflex.dto.ApiErrorDTO;
import jakarta.servlet.http.HttpServletResponse;
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
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        this.objectMapper = objectMapper;
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
               .exceptionHandling(exception -> exception
                       .authenticationEntryPoint((request, response, authException) -> {
                           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                           response.setContentType("application/json");
                           response.setCharacterEncoding("UTF-8");

                           ApiErrorDTO error = new ApiErrorDTO(
                                   HttpServletResponse.SC_UNAUTHORIZED,
                                   "UNAUTHORIZED",
                                   "Authentication required",
                                   LocalDateTime.now()
                           );

                           objectMapper.writeValue(response.getWriter(), error);
                       })
                       .accessDeniedHandler((request, response, accessDeniedException) -> {
                           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                           response.setContentType("application/json");
                           response.setCharacterEncoding("UTF-8");

                           ApiErrorDTO error = new ApiErrorDTO(
                                   HttpServletResponse.SC_FORBIDDEN,
                                   "FORBIDDEN",
                                   "You do not have permission to access this resource",
                                   LocalDateTime.now()
                           );

                           objectMapper.writeValue(response.getWriter(), error);
                       })
               )
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/error").permitAll()
                       //DataBase
                       .requestMatchers("/h2-console/**").permitAll()
                       .requestMatchers(
                               "/swagger-ui.html",
                               "/swagger-ui/**",
                               "/v3/api-docs/**"
                       ).permitAll()
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
