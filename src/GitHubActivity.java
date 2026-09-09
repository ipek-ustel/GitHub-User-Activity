import java.io.IOException;
import java.util.List;

public class GitHubActivity {

    public static void main(String[] args) {
        if (args.length < 1 || args[0].trim().isEmpty()) {
            System.err.println("Usage: github-activity <username>");
            System.exit(1);
        }

        String username = args[0].trim();
        GitHubApiClient client = new GitHubApiClient();

        try {
            String jsonResponse = client.fetchUserEvents(username);

            List<String> events = EventFormatter.formatEvents(jsonResponse);

            if (events.isEmpty()) {
                System.out.println("No recent public activity found for @" + username);
                return;
            }

            System.out.println("Recent activity for @" + username + ":");
            for (String event : events) {
                System.out.println(event);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Network error: Could not reach GitHub API (" + e.getMessage() + ")");
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Error: Request was interrupted.");
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }
}