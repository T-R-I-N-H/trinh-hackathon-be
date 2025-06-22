package trinh_be;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import trinh_be.utils.SpringContextUtils;

@SpringBootApplication(
		exclude = {
				MongoAutoConfiguration.class,
				MongoDataAutoConfiguration.class
		}
)
public class TrinhBeApplication {

	public static void main(String[] args) {
		SpringContextUtils.startSpringApplication(TrinhBeApplication.class, args);
	}

}
