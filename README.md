# Campus Course Records Manager (CCRM)

A comprehensive Java console application for managing campus courses, student enrollments, grades, and records with full file I/O operations.

## Project Overview

CCRM is a **Java SE console application** that demonstrates advanced Java programming concepts including OOP principles, design patterns, Stream API, NIO.2 file operations, and modern Java features.

**Key Features:**
- Student and Course management
- Enrollment system with business rules
- Grade recording and GPA calculation
- CSV import/export functionality
- Backup system with recursive utilities
- Comprehensive reporting

##  How to Run

### Prerequisites
- **JDK 17+** (Recommended: OpenJDK 17 or Oracle JDK 17)
- **VS Code** with Java Extension Pack
- Git (for version control)

### Installation & Execution
```bash

# Clone the repository
git clone https://github.com/abhigyansinha95/CampusCourseRecordsManager.git
cd CampusCourseRecordsManager

# Open in VS Code
code .

# Compile and run from VS Code terminal
cd src
javac edu\ccrm\io\*.java edu\ccrm\cli\*.java edu\ccrm\config\*.java
java edu.ccrm.cli.Main
```
### Evolution of Java

1995: Java 1.0 released - "Write Once, Run Anywhere"

1997: Java 1.1 - Inner classes, JDBC

2004: Java 5 - Generics, Enums, Autoboxing

2014: Java 8 - Lambdas, Stream API, Date/Time API

2017: Java 9 - Module system, JShell

2021: Java 17 (LTS) - Sealed classes, pattern matching

2023: Java 21 (LTS) - Virtual threads, pattern matching enhancements

### Java  Platform Comparison

![Java Platform Comparison](screenshots\table1.png)

### Java Architecture

JDK (Java Development Kit) – Tools + Compiler + JRE (for development).

JRE (Java Runtime Environment) – JVM + libraries (to run Java apps).

JVM (Java Virtual Machine) – Executes bytecode into machine code.

### Windows Installation Steps

# Step 1: Download JDK

Visit Oracle JDK Downloads

Download JDK 25 for Windows x64

Run the installer

# Step 2: Set Environment Variables

Right-click This PC → Properties → Advanced system settings

Click Environment Variables

Under System variables:

Create JAVA_HOME = C:\Program Files\Java\jdk-25\bin

Edit Path → Add %JAVA_HOME%\bin

# Step 3:Verify Installation

![Java Version Screenshot](screenshots/javaversion.png)

### VS Code Setup

# Step 1: Install VS Code Extensions

1. Open VS Code

2. Go to Extensions (Ctrl+Shift+X)

3. Search and install:

    Extension Pack for Java (by Microsoft)

    Project Manager for Java

    Debugger for Java

# Step 2: Open Project in VS Code
```bash

# Clone the repository
git clone https://github.com/abhigyansinha95/CampusCourseRecordsManager.git
cd CampusCourseRecordsManager
code .
```
# Step 3: Configure Java Runtime

1. Ctrl+Shift+P → "Java: Configure Java Runtime"

2. Select JDK 17+ as the runtime

3. VS Code will automatically detect the project structure

# Step 4: Running the Application
```bash
# Compile and run from VS Code terminal
cd src
javac edu\ccrm\io\*.java edu\ccrm\cli\*.java edu\ccrm\config\*.java
java edu.ccrm.cli.Main
```

### VS Code Structure

![VS Code Project Structure](screenshots\Folder_structure.png)

### Requirements Mapping Table

![VS Code Project Structure](screenshots\table2.png)

### Enabling Assertions
```bash
javac edu\ccrm\io\*.java edu\ccrm\cli\*.java edu\ccrm\config\*.java
java -ea edu.ccrm.cli.Main
```
### Project Working Example

![VS Code Project Working 1](screenshots\Working_1.png)


![VS Code Project Working 2](screenshots\Working_2.png)


![VS Code Project Working 3](screenshots\Working_3.png)

### Backup running

![Backup](screenshots\Backup.png)

This project is for educational purposes as part of academic coursework.

Developed by: Abhigyan Sinha
Course: Programming in Java
Institution: VIT Bhopal University

