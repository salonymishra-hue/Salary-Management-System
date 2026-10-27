# Salary Management System

## Project Overview

The Salary Management System is a web-based application developed using Java, JSP, Servlets, JDBC, and MySQL. It helps manage employee salary records and calculate net salaries.

## Features

* Add employee records
* View employee details
* Store employee information in a MySQL database
* Calculate net salary based on basic salary, allowances, and deductions
* Delete employee records
* Web-based user interface

## Technologies Used

* **Frontend:** HTML, CSS, JSP
* **Backend:** Java Servlets
* **Database:** MySQL
* **Database Connectivity:** JDBC
* **Build Tool:** Apache Maven
* **Server:** Apache Tomcat 10.1
* **Java Version:** Java 21

## Project Structure

```text
Salary-Management-System/
├── pom.xml
├── .gitignore
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── salary/
        │           ├── Employee.java
        │           └── EmployeeServlet.java
        └── webapp/
            ├── index.jsp
            ├── style.css
            └── WEB-INF/
                └── web.xml
```

## Database Setup

1. Install MySQL Server and start the MySQL service.
2. Open MySQL Workbench.
3. Create the database and employee table by executing:

```sql
CREATE DATABASE salary_management;

USE salary_management;

CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    allowances DECIMAL(10,2) DEFAULT 0,
    deductions DECIMAL(10,2) DEFAULT 0,
    net_salary DECIMAL(10,2) NOT NULL
);
```

4. Configure your MySQL connection details in `EmployeeServlet.java`.
5. Never publish your actual database password in a public repository.

## Net Salary Calculation

Net Salary = Basic Salary + Allowances − Deductions

**Example:**

* Basic Salary: ₹30,000
* Allowances: ₹5,000
* Deductions: ₹2,000
* **Net Salary: ₹33,000**

## How to Run

1. Install Java 21, Apache Maven, MySQL Server, and Apache Tomcat 10.1.
2. Configure the database connection.
3. Open a terminal in the project directory.
4. Build the project using:

```bash
mvn clean package
```

5. Deploy the generated WAR file from the `target` directory to Tomcat's `webapps` directory.
6. Start Tomcat and open:

```text
http://localhost:8080/salary-management-web/employees
```

## Author

MCA Student

## Academic Project

Developed as part of the Advanced Internet Programming (AIP) coursework.
