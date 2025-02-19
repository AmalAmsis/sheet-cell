# Sheet-Cell Project

## Overview
Sheet-Cell ("Shticell") is a **Java-based spreadsheet management system** that provides a **rich set of functionalities**, including **formula evaluation, sorting, filtering, range management, dynamic analysis, and real-time multi-user collaboration**. The project evolves through three major development stages:

1. **Console-Based Application** – A command-line interface for basic spreadsheet operations.
2. **Graphical User Interface (JavaFX)** – A rich UI allowing enhanced interactions.
3. **Client-Server Architecture** – Enabling multi-user collaboration via a server.

---

## Features

### 📝 General Spreadsheet Operations
- Load spreadsheet data from XML files
- Modify and update cell values
- Track and manage version history
- Apply formatting and styling

### 🔍 Data Management
- **Sorting**: Sort sheet data by multiple columns with user-defined priorities
- **Filtering**: Display only relevant data based on column values
- **Range Management**: Define and use named ranges in calculations
- **Dynamic Analysis**: Perform real-time "what-if" analysis with adjustable values

### 📈 Advanced Features
- **Graph Generation**: Create bar and line charts based on sheet data
- **Collaboration**: Share spreadsheets, set user permissions, and work in real-time
- **User Access Control**: Define read, write, and ownership permissions

---

## Project Structure
The project is modular and consists of the following key components:

### 1️⃣ **Engine Module**
- **Manages core spreadsheet logic**: cell dependencies, formulas, and calculations
- **Handles version control and change tracking**
- **Processes file uploads and ensures valid XML structures**

### 2️⃣ **Data Transfer Object (DTO) Module**
- Acts as a bridge between **client and server**
- Converts data into JSON for seamless network communication
- Supports **efficient data exchange** between system components

### 3️⃣ **Client Module**
- Implements the **Graphical User Interface (JavaFX)**
- Provides an intuitive user experience with **sorting, filtering, and dynamic analysis**
- Handles **user authentication and permissions management**

### 4️⃣ **Server Module**
- Built on **Tomcat**, enabling multi-user collaboration
- Manages **concurrent access, user roles, and permissions**
- Provides **RESTful API endpoints** for interacting with spreadsheets

---

## Installation and Setup

### **Prerequisites**
- Java 21
- JavaFX SDK 22.0.2
- Apache Tomcat
- Gson (for JSON handling)
- OkHttp (for HTTP requests)
- JAXB-RI (for XML parsing)

### **Running the Project**
#### 1️⃣ **Console-Based Version**
```sh
java -jar Engine.jar
```

#### 2️⃣ **JavaFX GUI Version**
```sh
java -jar UI.jar
```

#### 3️⃣ **Client-Server Version**
1. **Start the Server**
   ```sh
   java -jar Server.jar
   ```
2. **Run the Client**
   ```sh
   java -jar Client.jar
   ```

---

## Usage Guide

### 🔑 **User Authentication**
- Enter a **unique username** to log in
- If the username is taken, choose a different one

### 📊 **Managing Spreadsheets**
- **Load a new sheet** by selecting an XML file
- **View existing sheets** and their permission levels
- **Request access** from the sheet owner
- **Grant/Deny access** to other users

### 📄 **Editing Data**
- Select a **cell** to update its value
- Define **named ranges** for bulk operations
- Sort and filter data dynamically
- Perform **real-time what-if analysis** using sliders

### 📈 **Graph Generation**
- Choose data columns for **X and Y axes**
- Generate **bar or line graphs**
- Modify ranges dynamically for updated visuals

---

## Contribution Guidelines

Want to contribute? Here's how you can help:
1. **Fork the repository** and create a new branch
2. Implement changes and **submit a pull request**
3. Follow **Java coding conventions** and maintain clear documentation

---

## Authors
- **Amal Amsis**  
  Email: [amasisam@mta.ac.il](mailto:amasisam@mta.ac.il)
- **Yarden Daniel**  
  Email: [yardenda2@mta.ac.il](mailto:yardenda2@mta.ac.il)

---

## License
This project is licensed under the **MIT License** – feel free to modify and distribute it!

---

🚀 **Enjoy working with Sheet-Cell!** 🚀

