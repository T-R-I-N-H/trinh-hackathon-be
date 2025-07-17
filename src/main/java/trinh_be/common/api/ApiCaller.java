package trinh_be.common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiCaller {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(2))
            .build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T get(String url, Class<T> type) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
//                .timeout(Duration.ofMinutes(2))
                ;

        builder.header("Content-Type", "application/json");

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return mapper.readValue(response.body(), type);
    }

    public static <T> T post(String url, Object body, Class<T> type) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
//                .timeout(Duration.ofMinutes(2))
                ;

        builder.header("Content-Type", "application/json");

        HttpRequest request = builder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

