
package com.salary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://localhost:3306/salary_management";

    private static final String USER = "root";

    // Replace this with your actual MySQL root password.
    private static final String PASSWORD = "deepjyoti";

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(
                    "MySQL JDBC driver is missing. Check WEB-INF/lib in the deployed application.",
                    e
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Employee> employees = new ArrayList<>();

        String sql = """
                SELECT id, name, department, basic_salary,
                       allowances, deductions, net_salary
                FROM employees
                ORDER BY id DESC
                """;

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getBigDecimal("basic_salary"),
                        rs.getBigDecimal("allowances"),
                        rs.getBigDecimal("deductions"),
                        rs.getBigDecimal("net_salary")
                );

                employees.add(employee);
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to connect to MySQL or load employee records. Check the root cause in the Tomcat logs.",
                    e
            );
        }

        BigDecimal totalPayroll = BigDecimal.ZERO;

        for (Employee employee : employees) {
            totalPayroll = totalPayroll.add(employee.getNetSalary());
        }

        request.setAttribute("employees", employees);
        request.setAttribute("employeeCount", employees.size());
        request.setAttribute("totalPayroll", totalPayroll);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addEmployee(request);

            } else if ("delete".equals(action)) {
                deleteEmployee(request);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid action."
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/employees"
            );

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Database operation failed. Check your MySQL settings.",
                    e
            );
        }
    }

    private void addEmployee(HttpServletRequest request)
            throws SQLException {

        String name = request.getParameter("name");
        String department = request.getParameter("department");

        if (name == null || name.isBlank()
                || department == null || department.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee name and department are required."
            );
        }

        BigDecimal basic = readMoney(
                request.getParameter("basic_salary"),
                "Basic salary"
        );

        BigDecimal allowances = readMoney(
                request.getParameter("allowances"),
                "Allowances"
        );

        BigDecimal deductions = readMoney(
                request.getParameter("deductions"),
                "Deductions"
        );

        if (basic.signum() < 0
                || allowances.signum() < 0
                || deductions.signum() < 0) {
            throw new IllegalArgumentException(
                    "Salary amounts cannot be negative."
            );
        }

        BigDecimal grossSalary = basic.add(allowances);

        if (deductions.compareTo(grossSalary) > 0) {
            throw new IllegalArgumentException(
                    "Deductions cannot exceed the total salary."
            );
        }

        BigDecimal netSalary = grossSalary.subtract(deductions);

        String sql = """
                INSERT INTO employees
                (name, department, basic_salary, allowances,
                 deductions, net_salary)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name.trim());
            ps.setString(2, department.trim());
            ps.setBigDecimal(3, basic);
            ps.setBigDecimal(4, allowances);
            ps.setBigDecimal(5, deductions);
            ps.setBigDecimal(6, netSalary);

            ps.executeUpdate();
        }
    }

    private BigDecimal readMoney(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {
            BigDecimal amount = new BigDecimal(value);

            if (amount.scale() > 2) {
                throw new IllegalArgumentException(
                        fieldName + " can have at most two decimal places."
                );
            }

            return amount;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Enter a valid amount for " + fieldName + "."
            );
        }
    }

    private void deleteEmployee(HttpServletRequest request)
            throws SQLException {

        String value = request.getParameter("id");

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee ID is required."
            );
        }

        int id;

        try {
            id = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid employee ID."
            );
        }

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid employee ID."
            );
        }

        String sql = "DELETE FROM employees WHERE id = ?";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

