# GitHub User Activity CLI

A command-line interface (CLI) written in pure Java to fetch and display the recent public activity of any GitHub user.

---

## Features

- **Zero External Dependencies:** Built without third-party HTTP clients or JSON libraries (no Jackson, Gson, or Apache HttpComponents).
- **Modern Java HTTP Client:** Uses Java 11+ `HttpClient` with configured timeouts and required API headers.
- **Event Translation:** Converts raw GitHub events (`PushEvent`, `WatchEvent`, `IssuesEvent`, `CreateEvent`, `PullRequestEvent`, etc.) into clean, readable sentences.
- **Graceful Error Handling:** Handles nonexistent users (`404`), API rate limiting (`403`), and network dropouts cleanly without dumping stack traces.

---

## Prerequisites

- **Java Development Kit (JDK) 17+** (or Java 11 minimum) installed.
- Verify your installation: <br>
  java -version <br>
  javac -version

---

## Compilation & Build
Open your terminal in the root directory of the project (GitHub User Activity) and compile the source files from src/ into a bin/ output directory:

javac -d bin src/*.java

---

## Usage
Run the program using java, specifying the classpath (-cp bin) and passing a GitHub username:

java -cp bin GitHubActivity \<username>

**Example:** 

java -cp bin GitHubActivity ipek-ustel

**Example Output:**

Recent activity for @ipek-ustel: <br>
\- Pushed 1 commit(s) to ipek-ustel/TaskTracker <br>
\- Performed Public on ipek-ustel/TaskTracker

---

Project idea from: https://roadmap.sh/projects/github-user-activity
