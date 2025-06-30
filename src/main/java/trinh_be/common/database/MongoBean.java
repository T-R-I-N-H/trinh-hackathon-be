package trinh_be.common.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Slf4j
public class MongoBean {

    @Bean
    public MongoTemplate mongoTemplate() {
        try {
            Dotenv env = Dotenv.load();
            String url = env.get("DATABASE_URL");
            String database = env.get("DATABASE_NAME");

            ConnectionString connectionString = new ConnectionString(url);
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            MongoClient client = MongoClients.create(mongoClientSettings);
            log.info("Connected to MongoDB");
            return new MongoTemplate(client, database);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("Failed to connect to MongoDB", ex);
        }
    }
}
