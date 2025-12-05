package com.company.hrms.model;

public class Employee {
  private Integer id;
  private String name;
  private String department;
  private String position;
  private String phone;
  private String email;
  private String hireDate;
  private Double salary;
  private Integer userId;

  public Employee() {}

  public Employee(Integer id, String name, String department, String position, String phone, String email, String hireDate, Double salary, Integer userId) {
    this.id = id;
    this.name = name;
    this.department = department;
    this.position = position;
    this.phone = phone;
    this.email = email;
    this.hireDate = hireDate;
    this.salary = salary;
    this.userId = userId;
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDepartment() { return department; }
  public void setDepartment(String department) { this.department = department; }
  public String getPosition() { return position; }
  public void setPosition(String position) { this.position = position; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getHireDate() { return hireDate; }
  public void setHireDate(String hireDate) { this.hireDate = hireDate; }
  public Double getSalary() { return salary; }
  public void setSalary(Double salary) { this.salary = salary; }
  public Integer getUserId() { return userId; }
  public void setUserId(Integer userId) { this.userId = userId; }
}

