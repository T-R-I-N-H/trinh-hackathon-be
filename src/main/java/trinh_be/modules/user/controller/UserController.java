package trinh_be.modules.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trinh_be.common.api.ApiResponse;
import trinh_be.modules.auth.config.CustomUserDetails;
import trinh_be.modules.user.model.User;
import trinh_be.modules.user.service.UserService;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponse.success(UserService.getInstance().getByEmail(userDetails.getUsername()))
        );
    }
}
