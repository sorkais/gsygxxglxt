package com.company.hrms.dao;

import com.company.hrms.db.Database;
import com.company.hrms.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
  public List<Employee> listAll() throws SQLException {
    List<Employee> list = new ArrayList<>();
    try (Connection conn = Database.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("SELECT id, name, department, position, phone, email, hire_date, salary, user_id FROM employees ORDER BY id")) {
      while (rs.next()) {
        list.add(map(rs));
      }
    }
    return list;
  }

  public List<Employee> search(String keyword) throws SQLException {
    List<Employee> list = new ArrayList<>();
    String like = "%" + keyword + "%";
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "SELECT id, name, department, position, phone, email, hire_date, salary, user_id FROM employees WHERE name LIKE ? OR department LIKE ? OR position LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY id")) {
      for (int i = 1; i <= 5; i++) ps.setString(i, like);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) list.add(map(rs));
      }
    }
    return list;
  }

  public int insert(Employee e) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "INSERT INTO employees(name, department, position, phone, email, hire_date, salary, user_id) VALUES(?,?,?,?,?,?,?,?)",
             Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, e.getName());
      ps.setString(2, e.getDepartment());
      ps.setString(3, e.getPosition());
      ps.setString(4, e.getPhone());
      ps.setString(5, e.getEmail());
      ps.setString(6, e.getHireDate());
      if (e.getSalary() == null) ps.setNull(7, java.sql.Types.REAL); else ps.setDouble(7, e.getSalary());
      if (e.getUserId() == null) ps.setNull(8, java.sql.Types.INTEGER); else ps.setInt(8, e.getUserId());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) return rs.getInt(1);
      }
    }
    return -1;
  }

  public boolean update(Employee e) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "UPDATE employees SET name=?, department=?, position=?, phone=?, email=?, hire_date=?, salary=? WHERE id=?")) {
      ps.setString(1, e.getName());
      ps.setString(2, e.getDepartment());
      ps.setString(3, e.getPosition());
      ps.setString(4, e.getPhone());
      ps.setString(5, e.getEmail());
      ps.setString(6, e.getHireDate());
      if (e.getSalary() == null) ps.setNull(7, java.sql.Types.REAL); else ps.setDouble(7, e.getSalary());
      ps.setInt(8, e.getId());
      return ps.executeUpdate() > 0;
    }
  }

  public boolean delete(int id) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE id=?")) {
      ps.setInt(1, id);
      return ps.executeUpdate() > 0;
    }
  }

  private Employee map(ResultSet rs) throws SQLException {
    return new Employee(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("department"),
        rs.getString("position"),
        rs.getString("phone"),
        rs.getString("email"),
        rs.getString("hire_date"),
        rs.getObject("salary") == null ? null : rs.getDouble("salary"),
        rs.getObject("user_id") == null ? null : rs.getInt("user_id")
    );
  }

  public Employee findByUserId(int userId) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT id, name, department, position, phone, email, hire_date, salary, user_id FROM employees WHERE user_id=?")) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return map(rs);
      }
    }
    return null;
  }

  public boolean deleteByUserId(int userId) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE user_id=?")) {
      ps.setInt(1, userId);
      return ps.executeUpdate() > 0;
    }
  }
}

