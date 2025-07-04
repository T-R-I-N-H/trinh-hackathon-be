package trinh_be.modules.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trinh_be.common.api.ApiResponse;
import trinh_be.modules.auth.dto.LoginRequestDto;
import trinh_be.modules.auth.service.AuthService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Tag(name = "Authentication", description = "Authentication APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    private ResponseEntity<ApiResponse<String>> login(
            @RequestBody LoginRequestDto loginRequest
    ) throws GeneralSecurityException, IOException {

        String token = loginRequest.getGoogleIdToken();
        return ResponseEntity.ok(
               ApiResponse.success(AuthService.getInstance().login(token))
        );
    }
}
