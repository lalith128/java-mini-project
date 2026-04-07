# Online Quiz & Assessment System

A timer-based interactive quiz application built with **Core Java** and **Java Swing**, featuring admin question management, automatic scoring, and performance analysis.

## Features

- **Timed Quiz** – Configurable time per question with auto-advance
- **Admin Panel** – Add, edit, delete, and view questions (password: `admin`)
- **Performance Analysis** – Category-wise and difficulty-wise breakdown after each quiz
- **Result History** – View all past quiz results with scores, grades, and timestamps
- **Persistent Storage** – Questions and results are saved to disk using Java Serialization

## Prerequisites

- **Java JDK 8** or higher installed
- `javac` and `java` available in your system PATH

## How to Run

### 1. Compile

```bash
javac -d out src/*.java
```

### 2. Run

```bash
java -cp out QuizApp
```

### 3. Take the Quiz

1. Enter your name on the home screen
2. Set the number of questions and time per question
3. Click **Start Quiz** and answer the questions
4. After submission, review your answers and check the **Performance Analysis** tab

### 4. Admin Panel

1. Click **Admin Panel** on the home screen
2. Enter password: `admin`
3. Add, edit, or delete questions as needed

## Project Structure

```
JAVA-MINIPROJECT/
├── src/
│   ├── QuizApp.java       # Main application (Swing UI)
│   ├── Question.java       # Question model class
│   ├── QuizResult.java     # Result model with scoring & analytics
│   └── DataStore.java      # File-based persistence (serialization)
├── out/                    # Compiled .class files (generated)
├── questions.dat           # Saved questions (generated at runtime)
├── results.dat             # Saved results (generated at runtime)
└── README.md
```

## Technologies Used

- Java SE (Core Java)
- Java Swing (GUI)
- Java Serialization (Data Persistence)
