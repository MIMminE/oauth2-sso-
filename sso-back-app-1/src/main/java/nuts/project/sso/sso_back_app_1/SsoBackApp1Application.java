package nuts.project.sso.sso_back_app_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SsoBackApp1Application {

	public static void main(String[] args) {
		SpringApplication.run(SsoBackApp1Application.class, args);
	}

}
