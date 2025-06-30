package trinh_be.modules.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
public class User {
    @Id
    private String id;

    private String email;

    private String name;

    private String avatarUrl;
}
