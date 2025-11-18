package nuts.project.sso.sso_back_app_1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/back-app-1/health")
    public String healthCheck() {
        return "SSO Back App 1 is healthy";
    }
}