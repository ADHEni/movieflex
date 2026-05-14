package de.enricoprojects.movieflex.dto;

import java.time.LocalDateTime;

public record ApiErrorDTO(
        
        int status,
        String code,
        String message,
        LocalDateTime timestamp
        
) {
    
    
    
    
}
