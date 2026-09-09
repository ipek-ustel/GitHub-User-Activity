import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventFormatter {

    private static List<String> splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    objects.add(json.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return objects;
    }

    private static String formatEvent(String type, String repoName, String eventJson) {
        return switch (type) {
            case "PushEvent" -> {
                int commitCount = countOccurrences(eventJson, "\"sha\"\\s*:");
                yield "Pushed " + (commitCount > 0 ? commitCount : 1) + " commit(s) to " + repoName;
            }
            case "IssuesEvent" -> {
                String action = extractValue(eventJson, "\"action\"\\s*:\\s*\"([^\"]+)\"");
                yield capitalize(action != null ? action : "interacted with") + " an issue in " + repoName;
            }
            case "WatchEvent" -> "Starred " + repoName;
            case "ForkEvent" -> "Forked " + repoName;
            case "CreateEvent" -> {
                String refType = extractValue(eventJson, "\"ref_type\"\\s*:\\s*\"([^\"]+)\"");
                yield "Created " + (refType != null ? refType : "resource") + " in " + repoName;
            }
            case "PullRequestEvent" -> {
                String action = extractValue(eventJson, "\"action\"\\s*:\\s*\"([^\"]+)\"");
                yield capitalize(action != null ? action : "updated") + " a pull request in " + repoName;
            }
            default -> "Performed " + type.replace("Event", "") + " on " + repoName;
        };
    }

    private static String extractValue(String source, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static int countOccurrences(String source, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }


    public static List<String> formatEvents(String json) {
        List<String> output = new ArrayList<>();

        // Match individual JSON objects inside the top-level array
        List<String> eventObjects = splitJsonObjects(json);

        for (String eventJson : eventObjects) {
            String type = extractValue(eventJson, "\"type\"\\s*:\\s*\"([^\"]+)\"");
            String repoName = extractValue(eventJson, "\"repo\"\\s*:\\s*\\{[^}]*\"name\"\\s*:\\s*\"([^\"]+)\"");

            if (type == null || repoName == null) {
                continue;
            }

            String formattedMessage = formatEvent(type, repoName, eventJson);
            if (formattedMessage != null) {
                output.add("- " + formattedMessage);
            }
        }

        return output;
    }
    
}