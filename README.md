# Note Stash

A secure note-taking API built with Kotlin and Spring Boot. Secret Stash allows users to create, read, update, and delete their personal notes with JWT-based authentication.

## 🛠 Tech Stack
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-blueviolet?logo=kotlin)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=springboot)
![Java](https://img.shields.io/badge/Java-21-orange?logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.3-blue?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-red?logo=redis)
![Gradle](https://img.shields.io/badge/Gradle-Build%20Tool-02303A?logo=gradle)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.5.1-brightgreen?logo=springsecurity)
![Spring Actuator](https://img.shields.io/badge/Spring%20Actuator-Monitoring-brightgreen?logo=spring)
![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-%23ClojureGreen?logo=swagger)
![JUnit](https://img.shields.io/badge/JUnit-5-important?logo=java)
![Ktlint](https://img.shields.io/badge/Ktlint-1.7.1-blueviolet?logo=kotlin)
![Flyway](https://img.shields.io/badge/Flyway-DB%20Migration-orange?logo=flyway)
![JWT](https://img.shields.io/badge/JWT-0.11.5-blue?logo=jsonwebtokens)

## Features

- User authentication with JWT
- CRUD operations for notes
- Note expiry functionality
- Rate limiting to prevent brute-force attacks
- Redis caching for improved performance

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development without Docker)
- Gradle (for local development without Docker)

## Running with Docker

The easiest way to run Secret Stash is using Docker Compose, which will set up the application along with PostgreSQL and Redis.

### Quick Start

```bash
# Clone the repository
git clone https://github.com/tohidheshamti/secretstash.git
cd secretstash

# Start all services
docker compose up -d
```

check health:
curl http://localhost:8080/actuator/health

check logs: 
docker logs -f secret_stash_app


The application will be available at http://localhost:8080.

### Detailed Instructions

For detailed instructions on running the application, including:
- First-time setup
- Common operations (starting, stopping, viewing logs)
- Testing the API
- Troubleshooting
- Advanced configuration

Please see the [RUNNING.md](RUNNING.md) file.

## Environment Variables

The application can be configured using the following environment variables:

| Variable | Description | Default Value |
|----------|-------------|---------------|
| SPRING_DATASOURCE_URL | PostgreSQL connection URL | jdbc:postgresql://postgres-local:5432/secret_stash_local |
| SPRING_DATASOURCE_USERNAME | PostgreSQL username | root |
| SPRING_DATASOURCE_PASSWORD | PostgreSQL password | root |
| SPRING_DATA_REDIS_HOST | Redis host | redis |
| SPRING_DATA_REDIS_PORT | Redis port | 6379 |

## API Endpoints

### Authentication

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login and get JWT token

### Notes

- `GET /notes` - Get all notes for the authenticated user
- `GET /notes/{id}` - Get a specific note by ID
- `POST /notes` - Create a new note
- `PUT /notes/{id}` - Update an existing note
- `DELETE /notes/{id}` - Delete a note

## API Documentation with Swagger UI

Secret Stash includes Swagger UI for interactive API documentation and testing. Swagger UI provides a user-friendly interface to explore the API endpoints, understand the request/response formats, and test the API directly from your browser.

### Accessing Swagger UI

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

curl http://localhost:8080/actuator/health

### Features

- **Interactive Documentation**: Browse all available endpoints with detailed descriptions
- **Request Builder**: Construct API requests with a user-friendly form interface
- **Authentication Support**: Test secured endpoints by providing your JWT token
- **Response Visualization**: See the structure of API responses
- **Try it Out**: Execute API calls directly from the browser

### Using Swagger UI

1. Navigate to the Swagger UI URL
2. Browse the available endpoints grouped by controller
3. Click on an endpoint to expand its details
4. For authenticated endpoints:
   - First use the `/auth/login` endpoint to obtain a JWT token
   - Click the "Authorize" button at the top of the page
   - Enter your token in the format: `Bearer your-token-here`
   - Click "Authorize" to apply the token to all requests
5. Use the "Try it out" button to test endpoints with your parameters

## Local Development

### Prerequisites

- Java 21
- Gradle
- PostgreSQL
- Redis

### Setup

1. Start PostgreSQL and Redis:
```bash
docker compose up -d postgres-local redis
```

2. Run the application:
```bash
./gradlew bootRun
```

### Testing

```bash
./gradlew test
```

## Code Style

This project uses ktlint for code style checking and formatting. See the [ktlint section](#code-style-with-ktlint) for more details.

## Code Style with ktlint

This project uses [ktlint](https://github.com/pinterest/ktlint) for code style checking and formatting. ktlint is a Kotlin linter with built-in formatter that ensures code follows the official Kotlin code style.

### Available ktlint Tasks

The following Gradle tasks are available for ktlint:

- `./gradlew ktlintCheck` - Runs ktlint on your code and reports any style violations
- `./gradlew ktlintFormat` - Formats your code according to ktlint rules
- `./gradlew addKtlintCheckGitPreCommitHook` - Adds a Git pre-commit hook that runs ktlint check
- `./gradlew addKtlintFormatGitPreCommitHook` - Adds a Git pre-commit hook that runs ktlint format

### Configuration

ktlint is configured in two places:

1. **build.gradle.kts** - Contains the plugin configuration, including version, reporters, and filters
2. **.editorconfig** - Contains the code style rules for ktlint

## Architecture

Secret Stash follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Handles data access
- **Domain Layer**: Contains domain models

## License

[MIT License](LICENSE)
