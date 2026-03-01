# Practice 3: Process Thread Management

## Author
Luis Fernando Monjaraz Briseño

## Description
This Java program simulates a process management system with multi-threading, where each process (implemented as a Thread) consumes CPU and memory resources. The system includes:

- **Process Scheduling**: Multiple processes with different CPU usage, memory consumption, and priority levels
- **Memory Management**: Synchronization mechanism for safe memory allocation and deallocation
- **CPU Tracking**: Accumulates total CPU usage across all processes
- **Statistics**: Tracks maximum memory usage and number of executed processes
- **Thread Synchronization**: Uses synchronized methods to prevent race conditions

## Key Components
- `Proceso` class: Extends Thread to implement process behavior
- Static shared variables for memory and CPU management
- Comparator for sorting processes by priority
- ArrayList for storing processes sorted by priority

## Features
- Thread-safe memory allocation and release
- Synchronized statistics collection
- Priority-based process execution
- Real-time monitoring of system resources

## How to Compile and Run
```bash
javac process_thread_management.java
java process_thread_management
```

## Requirements
- Java 8 or higher
- JVM (Java Virtual Machine)

## License
Educational project
