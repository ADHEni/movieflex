package de.enricoprojects.movieflex.security;

import com.nimbusds.jwt.proc.ExpiredJWTException;
import de.enricoprojects.movieflex.dto.ApiErrorDTO;
import de.enricoprojects.movieflex.service.CustomUserDetailsService;
import de.enricoprojects.movieflex.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;


    public JwtAuthenticationFilter(JWTService jwtService, CustomUserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;

        }

        String jwtToken = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());


                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }


            }

            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException exception) {
            writeUnauthorizedResponse(
                    response,
                    "ACCESS_TOKEN_INVALID",
                    "Access token invalid"
            );
        }

    }


    private void writeUnauthorizedResponse(
            HttpServletResponse response,
            String code,
            String message
    ) throws IOException {

        ApiErrorDTO errorResponse = new ApiErrorDTO(
                HttpServletResponse.SC_UNAUTHORIZED,
                code,
                message,
                LocalDateTime.now()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}
