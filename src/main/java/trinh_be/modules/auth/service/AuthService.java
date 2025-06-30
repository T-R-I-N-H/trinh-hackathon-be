package trinh_be.modules.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import trinh_be.modules.user.model.User;
import trinh_be.modules.user.service.UserService;
import trinh_be.utils.JwtUtils;
import trinh_be.utils.SpringContextUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class AuthService {

    private String GOOGLE_CLIENT_ID;

    @PostConstruct
    private void init() {
        Dotenv env = Dotenv.load();
        GOOGLE_CLIENT_ID = env.get("GOOGLE_CLIENT_ID");
    }

    public String login(String googleIdToken) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = getGoogleTokenPayload(googleIdToken);
        if (payload == null) {
            throw new IllegalArgumentException("Invalid Google ID token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String avatar = (String) payload.get("picture");

        User user = UserService.getInstance().getByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAvatarUrl(avatar);
            UserService.getInstance().save(user);
        }

        return JwtUtils.generateToken(email);
    }

    private GoogleIdToken.Payload getGoogleTokenPayload(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
//                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            return idToken != null ? idToken.getPayload() : null;
        } catch (Exception e) {
            throw new GeneralSecurityException("Invalid Google ID token", e);
        }

    }

    public static AuthService getInstance() {
        return SpringContextUtils.getSingleton(AuthService.class);
    }
}
