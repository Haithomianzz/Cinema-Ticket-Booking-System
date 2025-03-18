# Cinema Booking System

The Cinema Booking System is a Java-based application designed to streamline and manage various cinema-related operations. It provides functionalities for seat management, showtime scheduling, ticket booking, and more, aiming to enhance the efficiency of cinema service delivery.

## Features

- **Seat Management**: Add, update, and retrieve seat information seamlessly.
- **Showtime Scheduling**: Schedule, reschedule, and cancel showtimes with ease.
- **Ticket Booking**: Book, update, and cancel tickets securely.
- **User Authentication**: Ensure secure access through robust authentication mechanisms.

## Getting Started

Follow these instructions to set up the project on your local machine for development and testing purposes.

### Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or higher.
- **JDBC**: Java Database Connector Library.
- **Apache Maven**: For dependency management and building the project.
- **SQL Server**: Database to store application data.

### Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/Haithomianzz/Cinema-Booking-System.git
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd Cinema-Booking-System
   ```

3. **Configure the Database**:

   - Create a SQL Server database named `cinema_booking_system` with suitable tables.
   - Update the database configuration in `src/java/com/client/DatabaseConnector.java` with your SQL Server credentials.

## Built With

- **JavaFX**: Framework for building Java-based applications.
- **JDBC**: Database connector for Java applications.
- **SQL Server**: Relational database management system.
- **Maven**: Dependency management and build automation tool.

## Contributing

We welcome contributions to enhance the project. Please follow these steps:

1. **Fork the Repository**.
2. **Create a New Branch**:

   ```bash
   git checkout -b feature/YourFeatureName
   ```

3. **Commit Your Changes**:

   ```bash
   git commit -m 'Add some feature'
   ```

4. **Push to the Branch**:

   ```bash
   git push origin feature/YourFeatureName
   ```

5. **Open a Pull Request**.
