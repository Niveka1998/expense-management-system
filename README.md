# EXPENSE-MANAGEMENT-SYSTEM

*Empower Your Finances, Master Your Future*

![Java](https://img.shields.io/badge/java-100%25-orange)
![Last Commit](https://img.shields.io/badge/last%20commit-today-brightgreen)
![Languages](https://img.shields.io/badge/languages-1-blue)
![XML](https://img.shields.io/badge/XML-Configuration-blue)

## 📋 Table of Contents

- [Overview](#overview)
- [Why Expense Management System?](#why-expense-management-system)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Testing](#testing)
- [Features](#features)
- [Architecture](#architecture)
- [Contributing](#contributing)

## 🎯 Overview


The Expense Management System is a microservices-based application built with Spring Boot and React, designed to streamline financial tracking and budgeting.

Each service is modular and independently deployable, connected through Eureka Server (service discovery) for seamless communication.

This project provides a scalable, maintainable solution for personal finance management, combining a strong backend with intuitive data handling.


## 🚀 Why Expense Management System?

This project simplifies backend development for financial applications by offering a modular, RESTful architecture with seamless MySQL integration. The core features include:

### 🔧 Core Features

- **🏗️ Layered Architecture**: Promotes clean separation of concerns with service interfaces, implementations, and controllers for scalable development.

- 🌟 Microservices Architecture – User Service, Expense Service, Budget Service, and Category Service, all connected via Eureka Server.

- 🌐 Service Discovery – Automatic detection and registration of services for easy scaling.

- 🗄️ MySQL Data Persistence – Reliable storage with Spring Data JPA.

- 📊 Entity & DTO Models – Clean separation of persistence and data exchange.

- 🔒 Validation & Error Handling – Ensures robust and secure data flow.

- ⚡ RESTful Endpoints – CRUD operations for users, expenses, budgets, and categories.

- 🔑 CORS Configuration – Smooth frontend–backend integration.

## 🛠️ Getting Started

### Prerequisites

Before running this project, ensure you have the following installed:

- **Programming Language**: Java (JDK 8 or higher)
- **Package Manager**: Maven
- **Database**: MySQL Server
- **IDE**: IntelliJ IDEA (recommended), Eclipse, or VS Code

### Installation

Build the expense-management-system from source and install dependencies:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Niveka1998/expense-management-system
   ```

2. **Navigate to the project directory:**
   ```bash
   cd expense-management-system
   ```

3. **Install the dependencies:**
   
   Using **Maven**:
   ```bash
   mvn install
   ```

4. **Configure the database:**
   - Create a MySQL database for the application
   - Update the `application.yml` file with your database credentials:
     ```properties
     spring:
       datasource:
         url: jdbc:mysql://localhost:3307/expenses_handler?createDatabaseIfNotExist=true
         username: your_username
         password: your_password
     ```

### Usage

Run the project with:

Using **Maven**:
```bash
mvn exec:java
```

Or alternatively:
```bash
mvn spring-boot:run
```


### Testing

Test the API endpoints using Postman to ensure everything is working correctly:

1. Start the Eureka server
2. Each service (user, expense, budget, category) registers with Eureka Server on http://localhost:8761. Run each service.
3. Import or create a Postman collection with the following endpoints:

**<h4>User Management:</h4>**

GET    /user/get-user      - Get user by email (send email in request body)

POST   /user/register-user - Register new user

**<h4>Expense Management:</h4>**

GET    /expense/get-all-expenses - Get all expenses

POST   /expense/add-new-expense  - Create new expense

PUT    /expense/update-expense   - Update expense

DELETE /expense/{id}             - Delete expense by ID

GET    /expense/{id}             - Get expense by ID

**<h4>Budget Management:</h4>**

GET    /budget/get-all-budgets - Get all budgets

POST   /budget/add-new-budget  - Create new budget

PUT    /budget/update-budget   - Update budget

DELETE /budget/{id}            - Delete budget by ID

GET    /budget/{id}            - Get budget by ID

**<h4>Category Management:</h4>**

GET    /category/get-all-categories - Get all categories

POST   /category/add-new-category   - Create new category

PUT    /category/update-category    - Update category

DELETE /category/{id}               - Delete category by ID

GET    /category/{id}               - Get category by ID

## ✨ Features

- **User Management**: Create and manage user accounts with authentication
- **Expense Tracking**: Record, categorize, and track daily expenses
- **Budget Management**: Set and monitor budget limits across different categories
- **Category System**: Organize expenses with customizable categories
- **RESTful APIs**: Complete API endpoints for all operations
- **Data Validation**: Robust input validation and error handling
- **Microservices Communication** – Services interact via Eureka

## 🏛️ Architecture

The system follows layered + microservice architecture patterns:

```
├── User Service        (User management APIs)  
├── Expense Service     (Expense APIs)  
├── Budget Service      (Budget APIs)  
├── Category Service    (Category APIs)  
├── Eureka Server       (Service Discovery)  
└── Gateway             (Central entry point)  
```

**Technology Stack:**
- **Backend**: Spring Boot, Spring Data JPA
- **Database**: MySQL
- **Build Tool**: Maven
- **Configuration**: XML-based configuration
- **API Documentation**: RESTful endpoints
- **Service Discovery**: Eureka Server
- **Frontend**: React + TypeScript (Axios for API calls)

If you encounter any issues or have questions, please:
- Check the [Issues](https://github.com/Niveka1998/expense-management-system/issues) page
- Create a new issue if your problem isn't already reported
- Provide detailed information about your environment and the steps to reproduce the issue

---

**Made with ❤️ for better financial management**











