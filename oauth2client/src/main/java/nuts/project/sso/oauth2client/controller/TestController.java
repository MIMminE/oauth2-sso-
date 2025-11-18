package nuts.project.sso.oauth2client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.net.URI;
import java.time.Instant;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class TestController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final ConcurrentHashMap<String, StoredToken> sessionStore = new ConcurrentHashMap<>();

    public TestController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/secure/test")
    public String test() {
        return "OAuth2 Client is working!";
    }

    @GetMapping("/get_test_front")
    public ResponseEntity<Void> getTestFront(Authentication authentication) {
        // 인증된 OAuth2 토큰이 있으면 서버에 저장하고 HttpOnly 세션 쿠키로 세션 id를 발급하여 프런트로 전달
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();

            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(registrationId, principalName);
            if (client != null && client.getAccessToken() != null) {
                OAuth2AccessToken accessToken = client.getAccessToken();
                OAuth2RefreshToken refreshToken = client.getRefreshToken();

                Map<String, Object> tokenJson = new HashMap<>();
                tokenJson.put("access_token", accessToken.getTokenValue());
                tokenJson.put("token_type", accessToken.getTokenType().getValue());
                tokenJson.put("issued_at", accessToken.getIssuedAt());
                tokenJson.put("expires_at", accessToken.getExpiresAt());
                if (refreshToken != null) tokenJson.put("refresh_token", refreshToken.getTokenValue());
                tokenJson.put("principal", principalName);
                tokenJson.put("registration", registrationId);

                String sessionId = UUID.randomUUID().toString();
                Instant expireAt = accessToken.getExpiresAt() != null ? accessToken.getExpiresAt() : Instant.now().plusSeconds(3600);
                sessionStore.put(sessionId, new StoredToken(tokenJson, expireAt));

                // 쿠키 만료시간 계산
                long maxAge = Math.max(30, Duration.between(Instant.now(), expireAt).getSeconds());

                // 로컬 개발: secure=false (프로덕션에서는 반드시 secure=true, sameSite, domain 설정)
                ResponseCookie cookie = ResponseCookie.from("SSO_SESSION", sessionId)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .sameSite("Lax")
                        .maxAge(maxAge)
                        .build();

                URI redirectUri = URI.create("http://localhost:5173/");
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .location(redirectUri)
                        .build();
            }
        }

        // 인증 정보가 없거나 토큰이 없으면 일반 리다이렉트
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:5173/"))
                .build();
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> sessionInfo(@CookieValue(name = "SSO_SESSION", required = false) String sessionId) {
        if (sessionId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        StoredToken stored = sessionStore.get(sessionId);
        if (stored == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (stored.expireAt.isBefore(Instant.now())) {
            sessionStore.remove(sessionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> resp = new HashMap<>();
        // 최소한의 정보만 반환 (JS에서 읽혀도 무해한 수준)
        resp.put("principal", stored.tokenJson.get("principal"));
        resp.put("registration", stored.tokenJson.get("registration"));
        resp.put("expires_at", stored.tokenJson.get("expires_at"));

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/auth/claim")
    public ResponseEntity<Map<String, Object>> claim(@RequestBody Map<String, String> req) {
        // 기존 방식 유지(선택적) - 현재 BFF 패턴에서는 사용하지 않음
        String ticket = req.get("ticket");
        if (ticket == null) return ResponseEntity.badRequest().build();

        StoredToken stored = sessionStore.remove(ticket);
        if (stored == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (stored.expireAt.isBefore(Instant.now())) return ResponseEntity.status(HttpStatus.GONE).build();

        return ResponseEntity.ok(stored.tokenJson);
    }

    private static class StoredToken {
        final Map<String, Object> tokenJson;
        final Instant expireAt;

        StoredToken(Map<String, Object> tokenJson, Instant expireAt) {
            this.tokenJson = tokenJson;
            this.expireAt = expireAt;
        }
    }
}
