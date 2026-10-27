
package com.salary;

import java.math.BigDecimal;

public class Employee {

    private int id;
    private String name;
    private String department;
    private BigDecimal basicSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netSalary;

    public Employee(
            int id,
            String name,
            String department,
            BigDecimal basicSalary,
            BigDecimal allowances,
            BigDecimal deductions,
            BigDecimal netSalary) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public BigDecimal getAllowances() {
        return allowances;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }
}

