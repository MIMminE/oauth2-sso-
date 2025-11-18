package nuts.project.sso.sso_back_app_2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/back-app-2/health")
    public String healthCheck() {
        return "SSO Back App 2 is healthy";
    }
}