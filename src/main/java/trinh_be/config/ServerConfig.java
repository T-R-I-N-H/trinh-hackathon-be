package trinh_be.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ServerConfig {
    private static final String AI_URL;
    private static final String AI_API_KEY;
    private static final String FE_URL;

    static {
        Dotenv env = Dotenv.load();

        AI_URL = env.get("AI_URL");
        AI_API_KEY = env.get("AI_API_KEY");
        FE_URL = env.get("FE_URL");
    }

    public static void main(String[] args) {
        System.out.println("AI URL: " + AI_URL);
        System.out.println("AI API Key: " + AI_API_KEY);
        System.out.println("Frontend URL: " + FE_URL);
    }
}
