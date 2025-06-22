package trinh_be.utils;


import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

@UtilityClass
public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public static void startSpringApplication(Class clazz, String[] args) {
        SpringApplication app = new SpringApplication(clazz);
        applicationContext = app.run(args);
    }

    public static <T> T getSingleton(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
