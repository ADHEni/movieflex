package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.entity.User;
import de.enricoprojects.movieflex.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class loginController {

    private final PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public loginController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public void registerUser(@RequestParam String username, @RequestParam String password,
                             @RequestParam String email) {




            User name = userRepository.findByUsername(username);

            User emailUser = userRepository.findByEmail(email);

            //TODO finish register

    }



}
