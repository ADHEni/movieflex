package de.enricoprojects.movieflex.dto;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {


    private String refreshToken;

    public RefreshTokenRequestDTO(String refreshToken) {

        this.refreshToken = refreshToken;

    }

}
