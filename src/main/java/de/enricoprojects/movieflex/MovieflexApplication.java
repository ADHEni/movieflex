package de.enricoprojects.movieflex;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieflexApplication {

	public static void main(String[] args) {


        Dotenv dotenv = Dotenv.configure().filename(".env").ignoreIfMissing().load();

        String dbUser = dotenv.get("DB_USERNAME");
        if(dbUser != null && !dbUser.isEmpty()){
            System.setProperty("DB_USERNAME", dbUser);
        }

        String dbPassword = dotenv.get("DB_PASSWORD");
        if(dbPassword != null && !dbPassword.isEmpty()){
            System.setProperty("DB_PASSWORD", dbPassword);
        }


        SpringApplication.run(MovieflexApplication.class, args);
	}

}
