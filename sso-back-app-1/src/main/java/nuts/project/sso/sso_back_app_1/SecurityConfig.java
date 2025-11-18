package nuts.project.sso.sso_back_app_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        auth
                                -> auth
                                // explicitly allow the login page itself and related paths
                                .requestMatchers("/", "/login", "/login/**", "/favicon.ico", "/error").permitAll()
                                .anyRequest().authenticated())
                // use a custom login page URL so unauthenticated requests are redirected to /login (not directly to provider)
                .oauth2Login(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
        ;

        return http.build();
    }
}