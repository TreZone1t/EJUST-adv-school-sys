# School Management System (Client-Server Architecture)

## Overview
This is a multi-threaded Java Client-Server application designed for managing a school system. It uses object-based communication by  (`java.io.Serializable`) via TCP sockets.

## Codebase Explanation (What Happens Where)

To help you navigate the project and discuss it comfortably, here is a detailed breakdown of every component in the codebase:

### 1. `client` Package
- **`MainUI.java`**: This is the entry point for the user. It connects to the server and runs an infinite loop showing menus based on the user's role. It uses `instanceof` (Polymorphism) to check if the `loggedInUser` is a `Manager`, `Teacher`, or `Student` and displays the correct menu. It captures console input and sends `Request` objects to the server.
- **`ClientNetwork.java`**: Handles the actual socket connection on the client side. It connects to `localhost:9090`, sends a `Request` object over the `ObjectOutputStream`, waits for a `Response` object from the server, and returns it to `MainUI`.

### 2. `server` Package
- **`ServerMain.java`**: The entry point for the server. It opens a `ServerSocket` on port `9090`. Whenever a new client connects, it accepts the connection and immediately spawns a new `ClientHandler` thread (Multi-Threading) so the server can handle multiple users at the same time.
- **`ClientHandler.java`**: A Thread that listens to a specific client. It reads the incoming `Request` object, checks the action (e.g., `LOGIN`, `CREATE_EXAM`). It includes a central **Authorization Check** using `loggedInUser.canPerform(action)`. If the action is allowed, it calls the `DataManager` to execute it and sends back a `Response` object.
- **`DataManager.java`**: The core database manager. It loads data from `.csv` files when the server starts and saves data back to them when changes happen. It handles object creation (like instantiating `Manager`, `Teacher`, or `Student` when parsing the users CSV) and manages `ArrayList` collections in memory.

### 3. `schema` Package (Shared Data Models)
- **`ICsvExportable.java`**: An **Interface** that enforces any implementing class to provide a `toCsv()` method.
- **`User.java`**: An **Abstract Class** that implements `ICsvExportable`. It holds `private` common fields (Encapsulation) and provides getters. It also defines an abstract method `getAllowedActions()` which forces subclasses to define their permissions.
- **`Manager.java`, `Teacher.java`, `Student.java`**: Subclasses that **Inherit** from `User`. Each overrides `getRole()` and `getAllowedActions()` (Polymorphism) to return their specific role name and their specific allowed actions array.
- **`Exam.java` & `Grade.java`**: Classes representing exams and grades. They implement `ICsvExportable` and encapsulate their data using private fields and getters/setters.
- **`Network.java`**: Contains the `Request` and `Response` wrapper classes used for Socket communication.

---

## OOP Requirements & Rubric Mapping

This project strictly adheres to the requested OOP guidelines:
1. **Proper use of classes and objects**: Entities like Users, Exams, and Grades are fully modeled as objects.
2. **Encapsulation**: All class fields are `private` and accessed only via getter methods (e.g., `getUsername()`, `getScore()`). Object creation is controlled via constructors.
3. **Inheritance**: `Manager`, `Teacher`, and `Student` inherit from the abstract `User` class to eliminate code duplication.
4. **Polymorphism**: The `getAllowedActions()` and `getRole()` methods are overridden in each subclass. `MainUI` uses `instanceof` to dynamically display menus.
5. **Interfaces & Abstract Classes**: The system uses `ICsvExportable` (Interface) and `User` (Abstract Class).
6. **Correct client–server communication using sockets**: Full Object Serialization via TCP/IP sockets (`ObjectInputStream` / `ObjectOutputStream`).
7. **File Handling and Data Structures**: Reading/Writing to CSV files and utilizing Java `ArrayList`s.
8. **Exception Handling**: Safe `try-catch` blocks throughout `ClientHandler`, `ClientNetwork`, and `DataManager`.
9. **Multi-Threading**: `ClientHandler` extends `Thread` to process multiple users simultaneously.

---

## Features
- **Role-Based Access Control:** Three distinct roles: Manager, Teacher, and Student.
- **Object-Based Communication:** Exchanging serialized Java objects instead of raw text.
- **Multi-threading:** Server handles multiple concurrent client connections.
- **File-based Persistence:** Data (Users, Exams, Grades) is securely stored in `.csv` files.
- **Exam Management:** Teachers can create or upload quizzes. Students can take exams, and their grades are automatically recorded.

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

## versions
- **v2.0.1** (last version) adding Inheritance and Polymorphism and updating the bug that allow any one to access private data only user with a manager or teacher role can do.
- **v2.0.0** fixing a bug that make the user add whatever role in mind by adding some if cond ,formatting the some file to highlight the encapsulation funs i forget to mention them in the discussion .
- **v1.0.2** fixing some exiting issues ( i was making the user exit when he try to logout and not use the already exist Logout action)
- **v1.0.1** remove a bug (i tried to make it possible to type exit in the server terminal to exit but it is impossible because the server is waiting for a client to connect and print that data that mean it a read only terminal)
