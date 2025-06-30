package trinh_be.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import trinh_be.modules.user.model.User;
import trinh_be.utils.SpringContextUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MongoTemplate mongoTemplate;

    public User getByEmail(String email) {
        return mongoTemplate.findOne(
                new Query(Criteria.where("email").is(email)),
                User.class
        );
    }

    public User save(User user) {
        return mongoTemplate.save(user);
    }

    public static UserService getInstance() {
        return SpringContextUtils.getSingleton(UserService.class);
    }
}
