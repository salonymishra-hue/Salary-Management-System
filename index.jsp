
<%@ page import="java.util.List" %>
<%@ page import="com.salary.Employee" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Employee> employees =
            (List<Employee>) request.getAttribute("employees");

    Integer employeeCount =
            (Integer) request.getAttribute("employeeCount");

    BigDecimal totalPayroll =
            (BigDecimal) request.getAttribute("totalPayroll");

    if (employees == null) {
        employees = java.util.Collections.emptyList();
    }

    if (employeeCount == null) {
        employeeCount = 0;
    }

    if (totalPayroll == null) {
        totalPayroll = BigDecimal.ZERO;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Salary Management System</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/style.css">
</head>

<body>

<aside class="sidebar">
    <div class="brand">
        <div class="brand-icon">S</div>
        <div>
            <h2>SalaryPro</h2>
            <p>Management Portal</p>
        </div>
    </div>

    <p class="nav-heading">MAIN MENU</p>

    <nav>
        <a class="active"
           href="${pageContext.request.contextPath}/employees">
            <span>▦</span> Dashboard
        </a>

        <a href="#add-employee">
            <span>＋</span> Add Employee
        </a>

        <a href="#employee-list">
            <span>☷</span> Employee Records
        </a>
    </nav>

    <div class="sidebar-footer">
        <div class="avatar">A</div>
        <div>
            <strong>Administrator</strong>
            <small>Salary Management</small>
        </div>
    </div>
</aside>

<main class="main-content">

    <header class="topbar">
        <div>
            <p class="eyebrow">OVERVIEW</p>
            <h1>Dashboard</h1>
            <p class="subtitle">
                Manage your employees and monthly payroll in one place.
            </p>
        </div>

        <div class="admin-pill">
            <span class="status-dot"></span>
            Administrator
        </div>
    </header>

    <section class="stats">

        <div class="stat-card">
            <div class="stat-top">
                <span>Total Employees</span>
                <span class="stat-icon purple">♙</span>
            </div>

            <h2><%= employeeCount %></h2>

            <p class="stat-caption">Registered employees</p>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <span>Monthly Payroll</span>
                <span class="stat-icon green">₹</span>
            </div>

            <h2>₹<%= totalPayroll.toPlainString() %></h2>

            <p class="stat-caption">Combined net salaries</p>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <span>System Status</span>
                <span class="stat-icon blue">✓</span>
            </div>

            <h2 class="active-status">Active</h2>

            <p class="stat-caption">Salary management portal</p>
        </div>

    </section>

    <section class="panel" id="add-employee">

        <div class="section-heading">
            <div>
                <h2>Add New Employee</h2>
                <p>Enter employee details and monthly salary components.</p>
            </div>

            <span class="section-icon">＋</span>
        </div>

        <form action="${pageContext.request.contextPath}/employees"
              method="post">

            <input type="hidden" name="action" value="add">

            <div class="form-grid">

                <div class="form-group">
                    <label for="name">Employee Name</label>

                    <input type="text"
                           id="name"
                           name="name"
                           placeholder="Enter full name"
                           maxlength="100"
                           required>
                </div>

                <div class="form-group">
                    <label for="department">Department</label>

                    <select id="department" name="department" required>
                        <option value="">Select department</option>
                        <option value="IT">IT</option>
                        <option value="HR">Human Resources</option>
                        <option value="Finance">Finance</option>
                        <option value="Marketing">Marketing</option>
                        <option value="Sales">Sales</option>
                        <option value="Operations">Operations</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="basic_salary">Basic Salary (₹)</label>

                    <input type="number"
                           id="basic_salary"
                           name="basic_salary"
                           min="0"
                           step="0.01"
                           placeholder="0.00"
                           required>
                </div>

                <div class="form-group">
                    <label for="allowances">Allowances (₹)</label>

                    <input type="number"
                           id="allowances"
                           name="allowances"
                           min="0"
                           step="0.01"
                           value="0"
                           required>
                </div>

                <div class="form-group">
                    <label for="deductions">Deductions (₹)</label>

                    <input type="number"
                           id="deductions"
                           name="deductions"
                           min="0"
                           step="0.01"
                           value="0"
                           required>
                </div>

            </div>

            <div class="form-footer">
                <p>Net salary = Basic salary + Allowances − Deductions</p>

                <button type="submit" class="btn-primary">
                    + Add Employee
                </button>
            </div>

        </form>

    </section>

    <section class="panel" id="employee-list">

        <div class="section-heading">
            <div>
                <h2>Employee Records</h2>
                <p>View employee details and calculated net salaries.</p>
            </div>

            <span class="record-count">
                <%= employeeCount %> Records
            </span>
        </div>

        <div class="table-wrapper">

            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Employee</th>
                        <th>Department</th>
                        <th>Basic Salary</th>
                        <th>Allowances</th>
                        <th>Deductions</th>
                        <th>Net Salary</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>

                <%
                    if (employees.isEmpty()) {
                %>

                    <tr>
                        <td colspan="8" class="empty-state">
                            <div class="empty-icon">☷</div>
                            <strong>No employee records yet</strong>
                            <p>Add your first employee using the form above.</p>
                        </td>
                    </tr>

                <%
                    } else {
                        for (Employee employee : employees) {
                %>

                    <tr>
                        <td class="id-cell">
                            <%= employee.getId() %>
                        </td>

                        <td>
                            <strong class="employee-name">
                                <%= employee.getName() %>
                            </strong>
                        </td>

                        <td>
                            <span class="department">
                                <%= employee.getDepartment() %>
                            </span>
                        </td>

                        <td>₹<%= employee.getBasicSalary().toPlainString() %></td>
                        <td>₹<%= employee.getAllowances().toPlainString() %></td>
                        <td>₹<%= employee.getDeductions().toPlainString() %></td>

                        <td class="salary-value">
                            ₹<%= employee.getNetSalary().toPlainString() %>
                        </td>

                        <td>
                            <form action="${pageContext.request.contextPath}/employees"
                                  method="post"
                                  onsubmit="return confirm('Are you sure you want to delete this employee?');">

                                <input type="hidden"
                                       name="action"
                                       value="delete">

                                <input type="hidden"
                                       name="id"
                                       value="<%= employee.getId() %>">

                                <button type="submit" class="btn-delete">
                                    Delete
                                </button>
                            </form>
                        </td>
                    </tr>

                <%
                        }
                    }
                %>

                </tbody>
            </table>

        </div>

    </section>

    <footer>
        <span>SalaryPro · Salary Management System</span>
        <span>MCA AIP Academic Project</span>
    </footer>

</main>

</body>
</html>
