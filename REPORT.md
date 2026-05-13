# Project Report: School Management System

## 1. Introduction
This report details the implementation of a robust, multi-threaded Client-Server School Management System. The project was developed in Java and satisfies the core requirements of network architecture, concurrent connection handling, and object-based data exchange.

## 2. Requirements Met
- **Client-Server Architecture:** The application successfully operates over TCP/IP sockets using `java.net.Socket` and `java.net.ServerSocket`.
- **Object Serialization:** Data communication between client and server relies on the exchange of objects utilizing `java.io.Serializable`. This is handled seamlessly through the `Network.Request` and `Network.Response` schemas, passing complex types rather than primitive strings.
- **Multi-Threading:** A `ClientHandler` thread is instantiated for every client connection, allowing the system to process requests from multiple users concurrently without blocking.
- **Persistence:** All system data (Users, Exams, and Grades) is written to and read from CSV files, ensuring that progress and state are retained across system restarts.

## 3. System Components
### 3.1 Server
The server initializes on port `9090` and infinitely listens for incoming connections. The `DataManager` class handles atomic access to the underlying storage files, mitigating race conditions that might arise from multiple threads interacting with the data simultaneously.

### 3.2 Client
The client connects to the server and manages local user interaction through `MainUI`. Upon valid authentication, the client sends object requests (like `CREATE_EXAM`, `UPLOAD_GRADE`, `TAKE_EXAM`) and awaits a structured `Response` object. The UI uses simple console input parsing.

### 3.3 Shared Data Schemas
By separating the `schema` models into a shared package, the system avoids `ClassNotFoundException` issues during deserialization. Shared components include `User`, `Grade`, `Exam`, and `Network`.

## 4. Conclusion
The project has successfully reached its objectives. It handles robust error-checking (e.g., verifying if the server is down before crashing) and fulfills all advanced grading requirements, particularly the proper utilization of `ObjectInputStream` and `ObjectOutputStream`.
