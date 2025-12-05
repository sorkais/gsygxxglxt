package com.company.hrms.dao;

import com.company.hrms.db.Database;
import com.company.hrms.model.User;
import com.company.hrms.security.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
  public User findByUsername(String username) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT id, username, password_hash, salt, role FROM users WHERE username=?")) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new User(
              rs.getInt("id"),
              rs.getString("username"),
              rs.getString("password_hash"),
              rs.getString("salt"),
              rs.getString("role")
          );
        }
      }
    }
    return null;
  }

  public int createUser(String username, String password, String role) throws SQLException {
    String salt = PasswordUtils.generateSaltBase64(16);
    String hash = PasswordUtils.hashPasswordBase64(password, salt);
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password_hash, salt, role) VALUES(?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, username);
      ps.setString(2, hash);
      ps.setString(3, salt);
      ps.setString(4, role);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
      return -1;
    }
  }

  public java.util.List<User> listAll() throws SQLException {
    java.util.List<User> list = new java.util.ArrayList<>();
    try (Connection conn = Database.getConnection();
         java.sql.Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("SELECT id, username, password_hash, salt, role FROM users ORDER BY id")) {
      while (rs.next()) list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("salt"), rs.getString("role")));
    }
    return list;
  }

  public java.util.List<User> search(String keyword) throws SQLException {
    java.util.List<User> list = new java.util.ArrayList<>();
    String like = "%" + keyword + "%";
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT id, username, password_hash, salt, role FROM users WHERE username LIKE ? OR role LIKE ? ORDER BY id")) {
      ps.setString(1, like);
      ps.setString(2, like);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"), rs.getString("salt"), rs.getString("role")));
      }
    }
    return list;
  }

  public boolean updateRole(int id, String role) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("UPDATE users SET role=? WHERE id=?")) {
      ps.setString(1, role);
      ps.setInt(2, id);
      return ps.executeUpdate() > 0;
    }
  }

  public boolean deleteById(int id) throws SQLException {
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
      ps.setInt(1, id);
      return ps.executeUpdate() > 0;
    }
  }

  public boolean changePassword(int id, String newPassword) throws SQLException {
    String salt = PasswordUtils.generateSaltBase64(16);
    String hash = PasswordUtils.hashPasswordBase64(newPassword, salt);
    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement("UPDATE users SET password_hash=?, salt=? WHERE id=?")) {
      ps.setString(1, hash);
      ps.setString(2, salt);
      ps.setInt(3, id);
      return ps.executeUpdate() > 0;
    }
  }
}

