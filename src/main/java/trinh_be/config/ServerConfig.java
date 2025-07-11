package trinh_be.config;

import io.github.cdimascio.dotenv.Dotenv;

public class ServerConfig {
    public static final String AI_VISUALIZE_URL;
    public static final String AI_CONVERSATION_URL;
    public static final String FE_URL;
    public static final String PARSER_URL;

    static {
        Dotenv env = Dotenv.load();

        AI_VISUALIZE_URL = env.get("AI_VISUALIZE_URL");
        AI_CONVERSATION_URL = env.get("AI_CONVERSATION_URL");
        FE_URL = env.get("FE_URL");
        PARSER_URL = env.get("PARSER_URL");
    }

    public static void main(String[] args) {
        System.out.println("Frontend URL: " + FE_URL);
    }
}
