package de.enricoprojects.movieflex.repository;

import de.enricoprojects.movieflex.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);


    User findByEmail(String email);


}
