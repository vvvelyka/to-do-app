# ToDo Application

This project is a REST API for managing ToDo items built with Spring Boot, PostgreSQL, and JWT (JSON Web Tokens) for secure access to protected resources.

The service includes user registration, login and CRUD operations for managing ToDo items.

## Key Features

- **User Registration:** Register users with a username and password
- **User Login:** Authenticate users and generate JWT tokens for secure access
- **JWT Authentication:** Protect endpoints by requiring valid JWT tokens.
- **Managing ToDo item:** CRUD operations

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.5** (JPA, Security, Web, Validation, OAuth2 Resource Server)
- **JWT (JSON Web Tokens)** for Authentication
- **Flyway** for database migrations
- **PostgreSQL** for persistent storage
- **Docker** for containerized services
- **Maven** for build automation


## Generating an RSA Key Pair with OpenSSL
To generate a new RSA key pair, use the following commands. Ensure that the keys are stored securely.

Change directory to where the keys should be stored:
   ```sh
   cd src/main/resources
   ```
Generate private key:
   ```sh
   openssl genpkey -algorithm RSA -out keystore.key -outform PEM
   ```
Generate public key:
   ```sh
   openssl rsa -pubout -in keystore.key -out app.pub
   ```

## How to run application

### Database Setup
 Pull latest Postgres Docker image:
   ```sh
   docker pull postgres
   ```
Run the Postgres container:
   ```sh
   docker run --name some-postgres -p 5432:5432
   ```
Create todo_dev database, schema will be created automatically on application startup if not exists.

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/vvvelyka/to-do-app.git
    cd to-do-app
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

### Test the APIs with Postman

Use Authorization tool to login with Basic Auth and communicate with the API using Bearer Token.

### Running Tests
To run the tests, use the following command:
```sh
mvn test
```

### Running with Docker

To run the application using Docker, follow these steps:

1. Build the Docker image:
    ```sh
    docker build -t todo-app .
    ```

2. Run the Docker container:
    ```sh
    docker run -p 8080:8080 todo-app
    ```

### Running with Docker Compose

To run the application using Docker Compose, follow these steps:

1. Build and start the services:
    ```sh
    docker-compose up --build
    ```

3. To stop the services, run:
    ```sh
    docker-compose stop
    ```