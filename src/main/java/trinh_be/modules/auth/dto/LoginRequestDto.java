package trinh_be.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @JsonProperty("google_id_token")
    private String googleIdToken;
}
