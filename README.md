# Multithreading and Asynchronous API Calls with Kotlin Spring Boot

The documentation is incomplete.

## Table of Contents
- [Introduction](#introduction)
- [Conclusion](#conclusion)
  - [Key Features](#key-features)
  - [How to Use](#how-to-use)
  - [Next Steps](#next-steps)
  - [Results](#results)
- [Documentation](#documentation)
  - [Technologies Used](#technologies-used)
  - [Setup Instructions](#setup-instructions)
  - [Project Structure](#project-structure)
  - [Additional Notes](#additional-notes)

## Introduction

In this project, we've developed a Kotlin Spring Boot application to test asynchronous API calls and demonstrate multithreading capabilities. The application interacts with a Flask API running locally to simulate HTTP requests.

For test part, we've developed a Python application with module of threading and another with concurrent.futures which sets settings at yaml format. 

## Conclusion

### Key Features
- **Multithreading:** Utilized Java 21 virtual threads to handle concurrent API requests efficiently.
- **Asynchronous API Calls:** Demonstrated how to make asynchronous HTTP GET requests using Spring's `RestTemplate`.
- **Thread Identification:** Included thread identifiers to differentiate threads calling the virtual threads API.
- **Performance Metrics:** Recorded start and end times for API calls to calculate runtime.
- **Different xxx:**
- **Test performance:**

### How to Use
1. Ensure that both the Kotlin Spring Boot application and the Flask API are running locally.

### Next Steps
- Explore further optimization techniques for multithreading and asynchronous processing.
- Experiment with different configurations and APIs to test performance under various conditions.
- Consider integrating additional monitoring and logging tools for more detailed analysis.

### Results
| Action              | Calls   | Threads | Time        |
|---------------------|---------|---------|-------------|
| Kotlin thread pool  | x1000   | x10     | 1584 ms     |
| Kotlin Multithreading| x1000   |  -      | 462 ms      |
| Python thread pool  | x1000   | x10     | 204.541 sec |
| Python concurrent   | x1000   |  x10      |     20.466 seconds       |



## Documentation

### Technologies Used
- Kotlin 1.9.22
- Spring Boot 3.2.3
- Java 21 Virtual Threads
- Python 3.9.0
- requests==2.26.0
- PyYAML==6.0
- Flask==2.0.2

### Setup Instructions
1. Clone the repository to your local machine.
2. Ensure you have JDK 17+ and Maven installed.
3. Navigate to the project directory.
4. Run `mvn spring-boot:run` to start the Kotlin Spring Boot application.
5. Ensure the Flask API server is running locally on port 5000 `http://localhost:5000/books/1`, `http://localhost:5000/books/2`.
6. Access `http://localhost:8080/test-api`, `http://localhost:8080/test-api21` to trigger the asynchronous API requests.
7. 

### Project Structure
- `src/main/kotlin/com/example/demo`: Contains Kotlin source files.
  - `Application.kt`: Entry point for the Spring Boot application.
  - `ApiController.kt`: Controller class handling API requests.
- `src/main/resources`: Contains application properties and configurations.
- `pom.xml`: Maven project configuration file.

### Additional Notes
- This project serves as a demonstration of multithreading and asynchronous processing in Kotlin Spring Boot applications and python......
- For any questions or issues, please open an [issue](https://github.com/ignasf5/java_python_http_test/issues) on GitHub.
