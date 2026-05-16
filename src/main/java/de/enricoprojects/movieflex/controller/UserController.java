package de.enricoprojects.movieflex.controller;

import de.enricoprojects.movieflex.dto.UserSummaryDTO;
import de.enricoprojects.movieflex.repository.UserRepository;
import de.enricoprojects.movieflex.service.CustomUserDetailsService;
import de.enricoprojects.movieflex.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;

    }

    @GetMapping
    public ResponseEntity<UserSummaryDTO> getUser(@AuthenticationPrincipal UserDetails userDetails) {

            return ResponseEntity.ok(userService.getUserSummaryDTO(userDetails.getUsername()));

    }


}
