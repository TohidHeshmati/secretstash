# Running Secret Stash

This document provides detailed instructions for running the Secret Stash application and its dependencies using Docker.

## Prerequisites

Before you begin, make sure you have the following installed on your system:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/) (usually included with Docker Desktop)

## First-Time Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/secretstash.git
   cd secretstash
   ```

2. Build and start all services:
   ```bash
   docker compose up -d
   ```
   This command:
   - Builds the application image from the Dockerfile
   - Pulls the Redis and PostgreSQL images if they're not already available
   - Creates and starts containers for all services
   - Sets up the necessary networks and volumes

3. Verify that all services are running:
   ```bash
   docker compose ps
   ```
   You should see all services (app, redis, postgres-local, postgres-test) with status "Up".

4. The application is now available at http://localhost:8080

## Common Operations

### Starting the Application

```bash
# Start all services
docker compose up -d

# Start only specific services
docker compose up -d redis postgres-local
```

### Stopping the Application

```bash
# Stop all services but keep containers
docker compose stop

# Stop all services and remove containers
docker compose down

# Stop all services, remove containers, and remove volumes (will delete all data)
docker compose down -v
```

### Viewing Logs

```bash
# View logs from all services
docker compose logs

# View logs from a specific service
docker compose logs app

# Follow logs in real-time
docker compose logs -f app

# Show last 100 lines of logs
docker compose logs --tail=100 app
```

### Rebuilding the Application

If you make changes to the code, you'll need to rebuild the application:

```bash
# Rebuild only the app service
docker compose build app

# Rebuild and restart the app service
docker compose up -d --build app
```

### Accessing the Database

```bash
# Connect to the PostgreSQL database
docker compose exec postgres-local psql -U root -d secret_stash_local
```

### Accessing Redis

```bash
# Connect to Redis CLI
docker compose exec redis redis-cli
```

## Testing the API

Once the application is running, you can test the API endpoints using curl or any API client like Postman.

### Register a User

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"securepass"}'
```

### Login and Get Token

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"securepass"}'
```

This will return a JWT token that you'll need for authenticated requests.

### Create a Note

```bash
curl -X POST http://localhost:8080/notes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{"title":"My First Note","content":"This is a test note"}'
```

### Get All Notes

```bash
curl -X GET http://localhost:8080/notes \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Data Persistence

The PostgreSQL data is persisted in a Docker volume named `postgres_data`. This means your data will survive container restarts and even removal.

To see all volumes:
```bash
docker volume ls
```

## Troubleshooting

### Application Won't Start

If the application container exits immediately or won't start:

1. Check the logs:
   ```bash
   docker compose logs app
   ```

2. Verify that PostgreSQL and Redis are running:
   ```bash
   docker compose ps
   ```

3. Make sure the database is accessible:
   ```bash
   docker compose exec postgres-local pg_isready
   ```

### Database Connection Issues

If the application can't connect to the database:

1. Check that the environment variables in docker-compose.yml match the PostgreSQL configuration
2. Try connecting to the database manually to verify it's working:
   ```bash
   docker compose exec postgres-local psql -U root -d secret_stash_local -c "SELECT 1"
   ```

### Redis Connection Issues

If the application can't connect to Redis:

1. Check that Redis is running:
   ```bash
   docker compose exec redis redis-cli ping
   ```
   It should respond with "PONG"

2. Verify the Redis connection settings in docker-compose.yml

### Port Conflicts

If you see errors about ports already being in use:

1. Check if any other applications are using the required ports (8080, 5432, 6379):
   ```bash
   # On Linux/macOS
   sudo lsof -i :8080
   sudo lsof -i :5432
   sudo lsof -i :6379
   
   # On Windows
   netstat -ano | findstr :8080
   netstat -ano | findstr :5432
   netstat -ano | findstr :6379
   ```

2. Stop the conflicting applications or change the port mappings in docker-compose.yml

## Advanced Configuration

### Changing Environment Variables

You can override the default environment variables in docker-compose.yml:

```bash
SPRING_DATASOURCE_PASSWORD=newpassword docker compose up -d
```

### Using External Services

If you want to use external PostgreSQL or Redis instances instead of the containerized ones:

1. Update the environment variables in docker-compose.yml to point to your external services
2. Remove the dependency on the containerized services:
   ```yaml
   depends_on:
     # Remove these lines if using external services
     - redis
     - postgres-local
   ```

## Cleanup

To completely remove all containers, networks, and volumes created by docker-compose:

```bash
docker compose down -v
```

Note: This will delete all data in the PostgreSQL database.
