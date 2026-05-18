package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.exception.InvalidCredentialsException;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public UserSummaryDTO getUserSummaryDTO(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException());


        return UserSummaryDTO.from(user);

    }

}
