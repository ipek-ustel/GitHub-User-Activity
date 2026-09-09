import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GitHubApiClient {
    private static final String GITHUB_API_URL = "https://api.github.com/users/%s/events";
    private final HttpClient client;

    public GitHubApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String fetchUserEvents(String username) throws IOException, InterruptedException {
        String url = String.format(GITHUB_API_URL, username);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "Java-GitHub-Activity-CLI")
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        return switch (statusCode) {
            case 200 -> response.body();
            case 404 -> throw new IllegalArgumentException("User '" + username + "' not found.");
            case 403 -> throw new IllegalStateException("Rate limit exceeded or access forbidden.");
            default -> throw new IOException("GitHub API returned unexpected status code: " + statusCode);
        };
    }


    
}