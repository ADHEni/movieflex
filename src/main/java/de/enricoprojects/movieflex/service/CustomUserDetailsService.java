package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.exception.InvalidCredentialsException;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public UserDetails loadUserByUsername(String username) throws InvalidCredentialsException {


        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(InvalidCredentialsException::new));


        return new org.springframework.security.core.userdetails.User(

                user.get().getUsername(),
                user.get().getPassword(),
                List.of(new SimpleGrantedAuthority(user.get().getRole()))


        );
    }

    public UserSummaryDTO getUserSummaryDTO(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException());


        return UserSummaryDTO.from(user);

    }

}
