# Kitchensink Application

A Spring Boot application that demonstrates MongoDB integration through REST endpoints and a web interface for managing members.

## **Prerequisites**

- Java 21
- Docker
- Maven

## **Setting Up MongoDB**

Start MongoDB using Docker:

```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

This will start MongoDB on port 27017.

The container can be removed using this command:

```bash
docker rm -f mongodb
```

## Building and Running the Project

All commands should be run from the project root directory.

Build the project with:

```bash
mvn clean package
```

Start the application with:

```bash
mvn spring-boot:run
```

## Testing the Application

### Using Web UI

1. Open your browser and navigate to `http://localhost:8080`
2. You'll see a member registration form with fields for:
    - Name (must not contain numbers)
    - Email (must be valid format)
    - Phone Number (10-12 digits)

### Using REST API

List all members:

```bash
curl http://localhost:8080/rest/members
```

Create a new member:

```bash
curl -X POST http://localhost:8080/rest/members \
-H "Content-Type: application/json" \
-d '{"name": "John Doe", "email": "john.doe@example.com","phoneNumber": "2125551212"}'
```

### Running Integration Tests

Execute the full test suite using:

```bash
mvn verify
```