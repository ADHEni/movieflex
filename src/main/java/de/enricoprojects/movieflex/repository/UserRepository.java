package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


    User findByEmail(String email);


}
