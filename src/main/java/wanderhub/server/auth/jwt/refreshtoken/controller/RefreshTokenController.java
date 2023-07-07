package wanderhub.server.auth.jwt.refreshtoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.global.response.MessageResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/v1/auth")
public class RefreshTokenController {

    private final TokenService tokenService;

    public RefreshTokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // 액세스 토큰 재발급.
    @PostMapping("/accessToken")
    public ResponseEntity getAccessToken(HttpServletRequest request, HttpServletResponse response) {    // RefreshToken 받아야됨.
        String email = request.getHeader("email");
        String refreshToken = request.getHeader("Authorization");
        String accessToken = tokenService.reissueAccessToken(email, refreshToken);
        response.setHeader("Access-Token", accessToken);
        return ResponseEntity.ok(new MessageResponseDto("Reissue AccessToken"));
    }

    // 로그 아웃
    @PostMapping("/logout")
    public ResponseEntity memberLogout(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        // 액세스 토큰 추출,
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        // principal에서 email 추출
        tokenService.logoutService(principal.getName(), accessToken);
        // 서비스에서 로그아웃 로직 수행.
        return ResponseEntity.ok(new MessageResponseDto("Logout Success."));
    }

}
