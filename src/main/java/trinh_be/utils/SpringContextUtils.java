package trinh_be.utils;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void startSpringApplication(Class clazz, String[] args) {
        SpringApplication.run(clazz, args);
    }

    public static <T> T getSingleton(Class<T> clazz) {
        if (applicationContext == null) {
            throw new IllegalStateException("Application context is not initialized yet");
        }
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
