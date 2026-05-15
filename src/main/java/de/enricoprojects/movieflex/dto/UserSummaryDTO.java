package de.enricoprojects.movieflex.dto;

import de.enricoprojects.movieflex.entity.User;
import lombok.Data;

@Data
public class UserSummaryDTO {


    private String username;
    private String email;

    public UserSummaryDTO(String username, String email) {

        this.username = username;
        this.email = email;


    }

    public static UserSummaryDTO from(User user){

        return new UserSummaryDTO(
                user.getUsername(),
                user.getEmail());



    }

}
