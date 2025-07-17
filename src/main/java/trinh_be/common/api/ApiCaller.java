package trinh_be.common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
public class ApiCaller {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(10))
            .build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T get(String url, Class<T> type) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofMinutes(10));

        builder.header("Content-Type", "application/json");

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("GET Request: {}", url);
        log.info("Response: {}", response.body());
        return mapper.readValue(response.body(), type);
    }

    public static <T> T post(String url, Object body, Class<T> type) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .timeout(Duration.ofMinutes(10));

        builder.header("Content-Type", "application/json");

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("POST Request: {}", mapper.writeValueAsString(body));
        log.info("Response: {}", response.body());
        return type == String.class ? (T) response.body() : mapper.readValue(response.body(), type);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "https://jsonplaceholder.typicode.com/posts/4";
        PostDto post = get(url, PostDto.class);
        System.out.println(post.title);

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostDto {
        public String title;
    }
}

