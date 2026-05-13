# School Management System (Client-Server Architecture)

## Overview
This is a multi-threaded Java Client-Server application designed for managing a school environment. It utilizes object-based communication (`java.io.Serializable`) via TCP sockets to ensure reliable, structured data exchange between the client and the server.

## Features
- **Role-Based Access Control:** Three distinct roles: Manager, Teacher, and Student.
- **Object-Based Communication:** Exchanging serialized Java objects instead of raw text.
- **Multi-threading:** Server handles multiple concurrent client connections.
- **File-based Persistence:** Data (Users, Exams, Grades) is securely stored in `.csv` files.
- **Exam Management:** Teachers can create or upload quizzes. Students can take exams, and their grades are automatically recorded.

## Architecture
- **Server:** Listens on port `9090`. Spawns a new `ClientHandler` thread for every incoming connection. Uses `DataManager` to read/write from local files.
- **Client:** Connects to the server, authenticates the user, and presents a customized UI (menu) depending on the user's role.
- **Schema:** Contains the shared objects (`User`, `Exam`, `Grade`, `Network.Request`, `Network.Response`) that both client and server understand.

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
   Open a new terminal and run:
   ```bash
   .\run_client.bat
   ```
