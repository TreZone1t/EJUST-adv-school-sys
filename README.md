# School Management System (Client-Server Architecture)

## Overview
This is a multi-threaded Java Client-Server application designed for managing a school system. It uses object-based communication by  (`java.io.Serializable`) via TCP sockets.

## Features
- **Role-Based Access Control:** Three distinct roles: Manager, Teacher, and Student.
- **Object-Based Communication:** Exchanging serialized Java objects instead of raw text.
- **Multi-threading:** Server handles multiple concurrent client connections.
- **File-based Persistence:** Data (Users, Exams, Grades) is securely stored in `.csv` files.
- **Exam Management:** Teachers can create or upload quizzes. Students can take exams, and their grades are automatically recorded.

## System Architecture & Components
- **Server:** Listens on port `9090`. Spawns a new `ClientHandler` thread for every incoming connection. Uses `DataManager` to read/write from local files. The `DataManager` class handles atomic access to the underlying storage files, mitigating race conditions that might arise from multiple threads interacting with the data simultaneously.
- **Client:** Connects to the server and manages local user interaction through `MainUI`. Upon valid authentication, the client sends object requests (like `CREATE_EXAM`, `UPLOAD_GRADE`, `TAKE_EXAM`) and awaits a structured `Response` object. The UI uses simple console input parsing.
- **Schema (Shared Data):** Contains the shared objects (`User`, `Exam`, `Grade`, `Network.Request`, `Network.Response`) that both client and server understand. By separating the `schema` models into a shared package, the system avoids `ClassNotFoundException` issues during deserialization.

## Requirements Met
- **Client-Server Architecture:** The application successfully operates over TCP/IP sockets using `java.net.Socket` and `java.net.ServerSocket`.
- **Object Serialization:** Data communication between client and server relies on the exchange of objects utilizing `java.io.Serializable`. This is handled seamlessly through the `Network.Request` and `Network.Response` schemas, passing complex types rather than primitive strings.
- **Multi-Threading:** A `ClientHandler` thread is instantiated for every client connection, allowing the system to process requests from multiple users concurrently without blocking.
- **Persistence:** All system data (Users, Exams, and Grades) is written to and read from CSV files, ensuring that progress and state are retained across system restarts.

## How to Run

1. **Compile the Project:**
   ```bash
   .\build.bat
   ```

2. **Start the Server:**
   ```bash
   .\run_server.bat
   ```

3. **Start the Client:**
   ```bash
   .\run_client.bat
   ```

## Conclusion
The project has successfully reached its objectives. It handles robust error-checking (e.g., verifying if the server is down before crashing) and fulfills all advanced requirements, particularly the proper utilization of `ObjectInputStream` and `ObjectOutputStream`.
