package nuts.project.sso.sso_back_app_1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    // Redis connection factory (Lettuce)
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

//    // Cookie serializer: set domain, SameSite, Secure, etc.
//    @Bean
//    public CookieSerializer cookieSerializer(@Value("${session.cookie.domain:}") String domain) {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setCookieName("SESSION");
//        if (domain != null && !domain.isBlank()) {
//            serializer.setDomainName(domain); // e.g. ".example.com"
//        }
//        serializer.setUseHttpOnlyCookie(true);
//        // If your apps are on different subdomains or different ports, SameSite=None may be required
//        serializer.setSameSite("None");
//        serializer.setUseSecureCookie(false); // change to true for HTTPS
//        return serializer;
//    }
}

