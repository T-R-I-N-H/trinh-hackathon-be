package trinh_be.common;

import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import trinh_be.utils.SpringContextUtils;

@Getter
@Component
@Slf4j
public class TMongoTemplate {
    private MongoTemplate template;

    @PostConstruct
    private void loadConfig() {
        try {
            Dotenv env = Dotenv.load();
            String url = env.get("DATABASE_URL");
            String database = env.get("DATABASE_NAME");

            ConnectionString connectionString = new ConnectionString(url);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            MongoClient client = MongoClients.create(mongoClientSettings);
            template = new MongoTemplate(client, database);
            log.info("Connected to MongoDB");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public static TMongoTemplate getInstance() {
        return SpringContextUtils.getSingleton(TMongoTemplate.class);
    }
}
